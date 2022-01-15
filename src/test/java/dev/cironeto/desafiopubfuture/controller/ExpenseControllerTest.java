package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import dev.cironeto.desafiopubfuture.dto.ExpenseDto;
import dev.cironeto.desafiopubfuture.dto.ExpenseInsertDto;
import dev.cironeto.desafiopubfuture.service.ExpenseService;
import dev.cironeto.desafiopubfuture.util.GetTotalExpensesReturnBody;
import dev.cironeto.desafiopubfuture.util.ExpenseDtoCreator;
import dev.cironeto.desafiopubfuture.util.ExpenseInsertDtoCreator;
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
@DisplayName("Tests for Expense Controller")
class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController expenseController;

    @Mock
    private ExpenseService expenseServiceMock;

    private Pageable pageable;
    private ExpenseInsertDto expenseInsertDto;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ExpenseType expenseType;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @BeforeEach
    void setUp() {
        expenseInsertDto = ExpenseInsertDtoCreator.createExpenseInsertDto();
        ExpenseDto expenseDto = ExpenseDtoCreator.createExpenseDto();
        expenseInsertDto = ExpenseInsertDtoCreator.createExpenseInsertDto();
        PageImpl<ExpenseDto> expenseDtoPage = new PageImpl<>(List.of(expenseDto));

        existingId = 2L;
        nonExistingId = 99L;
        dependentId = 90L;
        expenseType = ExpenseType.FOOD;
        dateFrom = LocalDate.parse("2021-06-13");
        dateTo = LocalDate.parse("2021-09-15");
        pageable = PageRequest.of(0, 10);

        Mockito.when(expenseServiceMock.getTotalExpenses()).thenReturn(new GetTotalExpensesReturnBody());

        Mockito.when(expenseServiceMock.findAllPaged(pageable)).thenReturn(expenseDtoPage);

        Mockito.when(expenseServiceMock.save(ArgumentMatchers.any())).thenReturn(expenseInsertDto);

        Mockito.doNothing().when(expenseServiceMock).delete(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(expenseServiceMock).delete(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(expenseServiceMock).delete(dependentId);

        Mockito.when(expenseServiceMock.filterByType(pageable, expenseType)).thenReturn(expenseDtoPage);

        Mockito.when(expenseServiceMock.filterByPaymentDate(dateFrom, dateTo, pageable))
                .thenReturn(expenseDtoPage);

        Mockito.when(expenseServiceMock.filterByDueDate(dateFrom, dateTo, pageable))
                .thenReturn(expenseDtoPage);
    }

    @Test
    @DisplayName("findAllPaged returns list of all expenses in page object when successful")
    void findAllPaged_ReturnsListOfAllExpensesInsidePage_WhenSuccessful() {
        ResponseEntity<Page<ExpenseDto>> page = expenseController.findAllPaged(pageable);

        Page<ExpenseDto> body = page.getBody();

        Assertions.assertThat(Objects.requireNonNull(body).toList())
                .isNotEmpty()
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(page.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(body.toList().get(0).getId())
                .isEqualTo(ExpenseDtoCreator.createExpenseDto().getId());
    }

    @Test
    @DisplayName("save persists the expense and returns an ExpenseDto when successful")
    void save_PersistsTheExpenseAndReturnsExpenseDto_WhenSuccessful() {
        ResponseEntity<ExpenseInsertDto> expenseSaved = expenseController.save(expenseInsertDto);

        Assertions.assertThat(expenseSaved.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ExpenseInsertDto body = expenseSaved.getBody();

        Assertions.assertThat(body)
                .isNotNull()
                .isEqualTo(ExpenseInsertDtoCreator.createExpenseInsertDto())
                .isInstanceOf(ExpenseInsertDto.class);
    }

    @Test
    @DisplayName("delete removes expense when successful")
    void delete_RemovesExpense_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    expenseController.delete(existingId);
                })
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = expenseController.delete(existingId);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when Id not found")
    void delete_ThrowsResourceNotFoundException_WhenIdNotFound() {
        Assertions.assertThatThrownBy(() -> expenseController.delete(nonExistingId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("delete throws DatabaseException when Id has dependent on other object")
    void delete_ThrowsDatabaseException_WhenIdDependsOnOtherObject() {
        Assertions.assertThatThrownBy(() -> expenseController.delete(dependentId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("update replaces an expense object with new information passed when successful")
    void update_ReplacesExpenseObject_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    expenseController.update(existingId, expenseInsertDto);
                })
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("getTotalExpenses returns GetTotalExpenseReturnBody with the sum of all expenses when successful")
    void getTotalExpenses_ReturnsTotalExpensesReturnBodyWithSumOfAllExpenses_WhenSuccessful() {
        ResponseEntity<GetTotalExpensesReturnBody> getTotalExpensesReturnBody =
                expenseController.getTotalExpenses();

        GetTotalExpensesReturnBody body = getTotalExpensesReturnBody.getBody();

        Assertions.assertThat(getTotalExpensesReturnBody.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        Assertions.assertThat(body)
                .isNotNull()
                .isInstanceOf(GetTotalExpensesReturnBody.class);
    }

    @Test
    @DisplayName("filterByType returns an expense list filtered by the expense type")
    void filterByType_ReturnsExpenseListFilteredByExpenseType_WhenSuccessful() {
        ResponseEntity<Page<ExpenseDto>> expenseDtoPage =
                expenseController.filterByType(pageable, expenseType);

        Assertions.assertThat(expenseDtoPage).isNotNull();
        Assertions.assertThat(expenseDtoPage.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("filterByReceivingDate returns an expense list filtered by receiving date between two dates")
    void filterByReceivingDate_ReturnExpenseListFilteredByReceivingDate_WhenDatesValid() {
        ResponseEntity<Page<ExpenseDto>> expenseDtoPage =
                expenseController.filterByPaymentDate(pageable, dateFrom, dateTo);

        Assertions.assertThat(expenseDtoPage).isNotNull();
        Assertions.assertThat(expenseDtoPage.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("filterByExpectedReceivingDate returns an expense list filtered by expected receiving date between two dates")
    void filterByExpectedReceivingDate_ReturnExpenseListFilteredByExpectedReceivingDate_WhenDatesValid() {
        ResponseEntity<Page<ExpenseDto>> expenseDtoPage =
                expenseController.filterByDueDate(pageable, dateFrom, dateTo);

        Assertions.assertThat(expenseDtoPage).isNotNull();
        Assertions.assertThat(expenseDtoPage.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}