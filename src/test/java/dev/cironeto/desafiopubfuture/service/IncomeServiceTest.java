package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.domain.Income;
import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import dev.cironeto.desafiopubfuture.dto.IncomeDto;
import dev.cironeto.desafiopubfuture.dto.IncomeInsertDto;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.repository.IncomeRepository;
import dev.cironeto.desafiopubfuture.service.exception.DatabaseException;
import dev.cironeto.desafiopubfuture.service.exception.ResourceNotFoundException;
import dev.cironeto.desafiopubfuture.util.AccountCreator;
import dev.cironeto.desafiopubfuture.util.GetTotalIncomesReturnBody;
import dev.cironeto.desafiopubfuture.util.IncomeCreator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Income Service")
class IncomeServiceTest {

    @InjectMocks
    private IncomeService incomeService;

    @Mock
    private IncomeRepository incomeRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

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
        Income income = IncomeCreator.createIncome();
        incomeInsertDto = IncomeInsertDtoCreator.createIncomeInsertDto();
        Account account = AccountCreator.createAccount();
        Long totalIncomes = 1000L;
        PageImpl<Income> incomePage = new PageImpl<>(List.of(income));

        existingId = 2L;
        nonExistingId = 99L;
        dependentId = 90L;
        incomeType = IncomeType.SALARY;
        dateFrom = LocalDate.parse("2021-06-13");
        dateTo = LocalDate.parse("2021-09-15");
        pageable = PageRequest.of(0, 10);

        Mockito.when(incomeRepositoryMock.getTotalIncomes()).thenReturn(totalIncomes);

        Mockito.when(incomeRepositoryMock.findAll(pageable)).thenReturn(incomePage);

        Mockito.when(incomeRepositoryMock.save(ArgumentMatchers.any())).thenReturn(income);

        Mockito.doNothing().when(incomeRepositoryMock).deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(incomeRepositoryMock).deleteById(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(incomeRepositoryMock).deleteById(dependentId);

        Mockito.when(incomeRepositoryMock.findByIncomeType(incomeType, pageable)).thenReturn(incomePage);

        Mockito.when(incomeRepositoryMock.filterByReceivingDate(dateFrom, dateTo, pageable)).thenReturn(incomePage);

        Mockito.when(incomeRepositoryMock.filterByExpectedReceivingDate(dateFrom, dateTo, pageable)).thenReturn(incomePage);

        Mockito.when(accountRepositoryMock.getById(ArgumentMatchers.any())).thenReturn(account);

        Mockito.when(incomeRepositoryMock.getById(ArgumentMatchers.any())).thenReturn(income);
    }

    @Test
    @DisplayName("findAllPaged returns list of all incomes in page object when successful")
    void findAllPaged_ReturnsListOfAllIncomesInsidePage_WhenSuccessful() {
        Page<IncomeDto> page = incomeService.findAllPaged(pageable);

        Assertions.assertThat(page).isNotEmpty();

        Assertions.assertThat(page.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(page.toList().get(0).getId())
                .isEqualTo(IncomeCreator.createIncome().getId());
    }

    @Test
    @DisplayName("save persists the income and returns an IncomeDto when successful")
    void save_PersistsTheIncomeAndReturnsIncomeDto_WhenSuccessful() {
        IncomeInsertDto incomeSaved = incomeService.save(incomeInsertDto);

        Assertions.assertThat(incomeSaved)
                .isNotNull()
                .isEqualTo(IncomeInsertDtoCreator.createIncomeInsertDto())
                .isInstanceOf(IncomeInsertDto.class);
    }

    @Test
    @DisplayName("delete removes income when successful")
    void delete_RemovesIncome_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    incomeService.delete(existingId);
                })
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when Id not found")
    void delete_ThrowsResourceNotFoundException_WhenIdNotFound() {
        Assertions.assertThatThrownBy(() -> incomeService.delete(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("delete throws DatabaseException when Id has dependent object")
    void delete_ThrowsDatabaseException_WhenIdNotFound() {
        Assertions.assertThatThrownBy(() -> incomeService.delete(dependentId))
                .isInstanceOf(DatabaseException.class);
    }

    @Test
    @DisplayName("update replaces an income object with new information passed when successful")
    void update_ReplacesIncomeObject_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    incomeService.update(existingId, incomeInsertDto);
                })
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("getTotalIncomes returns GetTotalIncomeReturnBody with the sum of all incomes when successful")
    void getTotalIncomes_ReturnsTotalIncomesReturnBodyWithSumOfAllIncomes_WhenSuccessful() {
        GetTotalIncomesReturnBody getTotalIncomesReturnBody = incomeService.getTotalIncomes();

        Assertions.assertThat(getTotalIncomesReturnBody)
                .isNotNull()
                .isInstanceOf(GetTotalIncomesReturnBody.class);
    }

    @Test
    @DisplayName("filterByType returns an income list filtered by the income type")
    void filterByType_ReturnsIncomeListFilteredByIncomeType_WhenSuccessful() {
        Page<IncomeDto> incomeDtoPage = incomeService.filterByType(pageable, incomeType);

        Assertions.assertThat(incomeDtoPage).isNotNull();
    }

    @Test
    @DisplayName("filterByReceivingDate returns an income list filtered by receiving date between two dates")
    void filterByReceivingDate_ReturnIncomeListFilteredByReceivingDate_WhenDatesValid() {
        Page<IncomeDto> incomeDtoPage = incomeService.filterByReceivingDate(dateFrom, dateTo, pageable);

        Assertions.assertThat(incomeDtoPage).isNotNull();
    }

    @Test
    @DisplayName("filterByExpectedReceivingDate returns an income list filtered by expected receiving date between two dates")
    void filterByExpectedReceivingDate_ReturnIncomeListFilteredByExpectedReceivingDate_WhenDatesValid() {
        Page<IncomeDto> incomeDtoPage = incomeService.filterByExpectedReceivingDate(dateFrom, dateTo, pageable);

        Assertions.assertThat(incomeDtoPage).isNotNull();
    }
}