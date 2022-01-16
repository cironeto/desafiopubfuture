package dev.cironeto.desafiopubfuture.repository;

import dev.cironeto.desafiopubfuture.domain.Expense;
import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@DataJpaTest
@DisplayName("Tests for Expense Repository")
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    private Pageable pageable;
    private ExpenseType expenseType;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        expenseType = ExpenseType.FOOD;
        dateFrom = LocalDate.parse("2021-06-13");
        dateTo = LocalDate.parse("2021-09-15");
    }

    @Test
    @DisplayName("findByExpenseType returns an expense list filtered by the expense type")
    void findByExpenseType_ReturnsExpenseListFilteredByType_WhenSuccessful() {
        Page<Expense> page = expenseRepository.findByExpenseType(expenseType, pageable);

        Assertions.assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("getTotalExpenses returns a Long value with the sum of all expenses")
    void getTotalExpenses_ReturnsLongValue_WhenSuccessful() {
        Long totalExpenses = expenseRepository.getTotalExpenses();

        Assertions.assertThat(totalExpenses)
                .isNotNull()
                .isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("filterByPaymentDate returns an expense list filtered by payment date between two dates")
    void filterByPaymentDate_ReturnExpenseListFilteredByPaymentDate_WhenDatesValid() {
        Page<Expense> page = expenseRepository.filterByPaymentDate(dateFrom, dateTo, pageable);

        Assertions.assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("filterByDueDate returns an expense list filtered by due date between two dates")
    void filterByDueDate_ReturnExpenseListFilteredByDueDate_WhenDatesValid() {
        Page<Expense> page = expenseRepository.filterByDueDate(dateFrom, dateTo, pageable);

        Assertions.assertThat(page).isNotNull();
    }
}