package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import dev.cironeto.desafiopubfuture.dto.IncomeDto;
import dev.cironeto.desafiopubfuture.dto.IncomeInsertDto;
import dev.cironeto.desafiopubfuture.service.IncomeService;
import dev.cironeto.desafiopubfuture.util.GetTotalIncomesReturnBody;
import dev.cironeto.desafiopubfuture.util.IncomeDtoCreator;
import dev.cironeto.desafiopubfuture.util.IncomeInsertDtoCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Income Controller")
class IncomeControllerTest {

    @InjectMocks
    private IncomeController incomeController;

    @Mock
    private IncomeService incomeServiceMock;

    private Pageable pageable;
    private IncomeInsertDto incomeInsertDto;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private IncomeType incomeType;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @BeforeEach
    void setUp() {
        incomeInsertDto = IncomeInsertDtoCreator.createIncomeInsertDto();
        IncomeDto incomeDto = IncomeDtoCreator.createIncomeDto();
        incomeInsertDto = IncomeInsertDtoCreator.createIncomeInsertDto();
        PageImpl<IncomeDto> incomeDtoPage = new PageImpl<>(List.of(incomeDto));

        existingId = 2L;
        nonExistingId = 99L;
        dependentId = 90L;
        incomeType = IncomeType.SALARY;
        dateFrom = LocalDate.parse("2021-06-13");
        dateTo = LocalDate.parse("2021-09-15");
        pageable = PageRequest.of(0, 10);

        Mockito.when(incomeServiceMock.getTotalIncomes()).thenReturn(new GetTotalIncomesReturnBody());

        Mockito.when(incomeServiceMock.findAllPaged(pageable)).thenReturn(incomeDtoPage);

        Mockito.when(incomeServiceMock.save(ArgumentMatchers.any())).thenReturn(incomeInsertDto);

        Mockito.doNothing().when(incomeServiceMock).delete(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(incomeServiceMock).delete(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(incomeServiceMock).delete(dependentId);

        Mockito.when(incomeServiceMock.filterByType(pageable, incomeType)).thenReturn(incomeDtoPage);

        Mockito.when(incomeServiceMock.filterByReceivingDate(dateFrom, dateTo, pageable))
                .thenReturn(incomeDtoPage);

        Mockito.when(incomeServiceMock.filterByExpectedReceivingDate(dateFrom, dateTo, pageable))
                .thenReturn(incomeDtoPage);
    }

    @Test
    @DisplayName("findAllPaged returns list of all incomes in page object when successful")
    void findAllPaged_ReturnsListOfAllIncomesInsidePage_WhenSuccessful() {
        ResponseEntity<Page<IncomeDto>> page = incomeController.findAllPaged(pageable);

        Page<IncomeDto> body = page.getBody();

        Assertions.assertThat(Objects.requireNonNull(body).toList())
                .isNotEmpty()
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(page.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(body.toList().get(0).getId())
                .isEqualTo(IncomeDtoCreator.createIncomeDto().getId());
    }

    @Test
    @DisplayName("save persists the income and returns an IncomeDto when successful")
    void save_PersistsTheIncomeAndReturnsIncomeDto_WhenSuccessful() {
        ResponseEntity<IncomeInsertDto> incomeSaved = incomeController.save(incomeInsertDto);

        Assertions.assertThat(incomeSaved.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        IncomeInsertDto body = incomeSaved.getBody();

        Assertions.assertThat(body)
                .isNotNull()
                .isEqualTo(IncomeInsertDtoCreator.createIncomeInsertDto())
                .isInstanceOf(IncomeInsertDto.class);
    }

    @Test
    @DisplayName("delete removes income when successful")
    void delete_RemovesIncome_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    incomeController.delete(existingId);
                })
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = incomeController.delete(existingId);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when Id not found")
    void delete_ThrowsResourceNotFoundException_WhenIdNotFound() {
        Assertions.assertThatThrownBy(() -> incomeController.delete(nonExistingId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("delete throws DatabaseException when Id has dependent on other object")
    void delete_ThrowsDatabaseException_WhenIdDependsOnOtherObject() {
        Assertions.assertThatThrownBy(() -> incomeController.delete(dependentId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("update replaces an income object with new information passed when successful")
    void update_ReplacesIncomeObject_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    incomeController.update(existingId, incomeInsertDto);
                })
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("getTotalIncomes returns GetTotalIncomeReturnBody with the sum of all incomes when successful")
    void getTotalIncomes_ReturnsTotalIncomesReturnBodyWithSumOfAllIncomes_WhenSuccessful() {
        ResponseEntity<GetTotalIncomesReturnBody> getTotalIncomesReturnBody =
                incomeController.getTotalIncomes();

        GetTotalIncomesReturnBody body = getTotalIncomesReturnBody.getBody();

        Assertions.assertThat(getTotalIncomesReturnBody.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        Assertions.assertThat(body)
                .isNotNull()
                .isInstanceOf(GetTotalIncomesReturnBody.class);
    }

    @Test
    @DisplayName("filterByType returns an income list filtered by the income type")
    void filterByType_ReturnsIncomeListFilteredByIncomeType_WhenSuccessful() {
        ResponseEntity<Page<IncomeDto>> incomeDtoPage =
                incomeController.filterByType(pageable, incomeType);

        Assertions.assertThat(incomeDtoPage).isNotNull();
        Assertions.assertThat(incomeDtoPage.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("filterByReceivingDate returns an income list filtered by receiving date between two dates")
    void filterByReceivingDate_ReturnIncomeListFilteredByReceivingDate_WhenDatesValid() {
        ResponseEntity<Page<IncomeDto>> incomeDtoPage =
                incomeController.filterByReceivingDate(pageable, dateFrom, dateTo);

        Assertions.assertThat(incomeDtoPage).isNotNull();
        Assertions.assertThat(incomeDtoPage.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("filterByExpectedReceivingDate returns an income list filtered by expected receiving date between two dates")
    void filterByExpectedReceivingDate_ReturnIncomeListFilteredByExpectedReceivingDate_WhenDatesValid() {
        ResponseEntity<Page<IncomeDto>> incomeDtoPage =
                incomeController.filterByExpectedReceivingDate(pageable, dateFrom, dateTo);

        Assertions.assertThat(incomeDtoPage).isNotNull();
        Assertions.assertThat(incomeDtoPage.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}