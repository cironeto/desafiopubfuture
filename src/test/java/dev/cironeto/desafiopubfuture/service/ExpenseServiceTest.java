package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.domain.Expense;
import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import dev.cironeto.desafiopubfuture.dto.ExpenseDto;
import dev.cironeto.desafiopubfuture.dto.ExpenseInsertDto;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.repository.ExpenseRepository;
import dev.cironeto.desafiopubfuture.service.exception.DatabaseException;
import dev.cironeto.desafiopubfuture.service.exception.ResourceNotFoundException;
import dev.cironeto.desafiopubfuture.util.AccountCreator;
import dev.cironeto.desafiopubfuture.util.GetTotalExpensesReturnBody;
import dev.cironeto.desafiopubfuture.util.ExpenseCreator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Expense Service")
class ExpenseServiceTest {

    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private ExpenseRepository expenseRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

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
        Expense expense = ExpenseCreator.createExpense();
        expenseInsertDto = ExpenseInsertDtoCreator.createExpenseInsertDto();
        Account account = AccountCreator.createAccount();
        Long totalExpenses = 1000L;
        PageImpl<Expense> expensePage = new PageImpl<>(List.of(expense));

        existingId = 2L;
        nonExistingId = 99L;
        dependentId = 90L;
        expenseType = ExpenseType.FOOD;
        dateFrom = LocalDate.parse("2021-06-13");
        dateTo = LocalDate.parse("2021-09-15");
        pageable = PageRequest.of(0, 10);

        Mockito.when(expenseRepositoryMock.getTotalExpenses()).thenReturn(totalExpenses);

        Mockito.when(expenseRepositoryMock.findAll(pageable)).thenReturn(expensePage);

        Mockito.when(expenseRepositoryMock.save(ArgumentMatchers.any())).thenReturn(expense);

        Mockito.doNothing().when(expenseRepositoryMock).deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(expenseRepositoryMock).deleteById(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(expenseRepositoryMock).deleteById(dependentId);

        Mockito.when(expenseRepositoryMock.findByExpenseType(expenseType, pageable)).thenReturn(expensePage);

        Mockito.when(expenseRepositoryMock.filterByPaymentDate(dateFrom, dateTo, pageable)).thenReturn(expensePage);

        Mockito.when(expenseRepositoryMock.filterByDueDate(dateFrom, dateTo, pageable)).thenReturn(expensePage);

        Mockito.when(accountRepositoryMock.getById(ArgumentMatchers.any())).thenReturn(account);

        Mockito.when(expenseRepositoryMock.getById(ArgumentMatchers.any())).thenReturn(expense);
    }

    @Test
    @DisplayName("findAllPaged returns list of all expenses in page object when successful")
    void findAllPaged_ReturnsListOfAllExpensesInsidePage_WhenSuccessful() {
        Page<ExpenseDto> page = expenseService.findAllPaged(pageable);

        Assertions.assertThat(page).isNotEmpty();

        Assertions.assertThat(page.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(page.toList().get(0).getId())
                .isEqualTo(ExpenseCreator.createExpense().getId());
    }

    @Test
    @DisplayName("save persists the expense and returns an ExpenseDto when successful")
    void save_PersistsTheExpenseAndReturnsExpenseDto_WhenSuccessful() {
        ExpenseInsertDto expenseSaved = expenseService.save(expenseInsertDto);

        Assertions.assertThat(expenseSaved)
                .isNotNull()
                .isEqualTo(ExpenseInsertDtoCreator.createExpenseInsertDto())
                .isInstanceOf(ExpenseInsertDto.class);
    }

    @Test
    @DisplayName("delete removes expense when successful")
    void delete_RemovesExpense_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    expenseService.delete(existingId);
                })
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when Id not found")
    void delete_ThrowsResourceNotFoundException_WhenIdNotFound() {
        Assertions.assertThatThrownBy(() -> expenseService.delete(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("delete throws DatabaseException when Id has dependent objects")
    void delete_ThrowsDatabaseException_WhenHasDependentObjects() {
        Assertions.assertThatThrownBy(() -> expenseService.delete(dependentId))
                .isInstanceOf(DatabaseException.class);
    }

    @Test
    @DisplayName("update replaces an expense object with new information when successful")
    void update_ReplacesExpenseObject_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    expenseService.update(existingId, expenseInsertDto);
                })
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("getTotalExpenses returns GetTotalExpenseReturnBody with the sum of all expenses when successful")
    void getTotalExpenses_ReturnsTotalExpensesReturnBodyWithSumOfAllExpenses_WhenSuccessful() {
        GetTotalExpensesReturnBody getTotalExpensesReturnBody = expenseService.getTotalExpenses();

        Assertions.assertThat(getTotalExpensesReturnBody)
                .isNotNull()
                .isInstanceOf(GetTotalExpensesReturnBody.class);
    }

    @Test
    @DisplayName("filterByType returns an expense list filtered by the expense type")
    void filterByType_ReturnsExpenseListFilteredByExpenseType_WhenSuccessful() {
        Page<ExpenseDto> expenseDtoPage = expenseService.filterByType(pageable, expenseType);

        Assertions.assertThat(expenseDtoPage).isNotNull();
    }

    @Test
    @DisplayName("filterByPaymentDate returns an expense list filtered by payment date between two dates")
    void filterByPaymentDate_ReturnExpenseListFilteredByPaymentDate_WhenDatesValid() {
        Page<ExpenseDto> expenseDtoPage = expenseService.filterByPaymentDate(dateFrom, dateTo, pageable);

        Assertions.assertThat(expenseDtoPage).isNotNull();
    }

    @Test
    @DisplayName("filterByDueDate returns an expense list filtered by due date between two dates")
    void filterByDueDate_ReturnExpenseListFilteredByDueDate_WhenDatesValid() {
        Page<ExpenseDto> expenseDtoPage = expenseService.filterByDueDate(dateFrom, dateTo, pageable);

        Assertions.assertThat(expenseDtoPage).isNotNull();
    }
}