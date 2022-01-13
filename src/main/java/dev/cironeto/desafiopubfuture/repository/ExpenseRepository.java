package dev.cironeto.desafiopubfuture.repository;

import dev.cironeto.desafiopubfuture.domain.Expense;
import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByExpenseType(ExpenseType expenseType, Pageable pageable);

    @Query(value = "SELECT SUM(value) FROM Expense")
    Long getTotalExpenses();

    @Query("SELECT e FROM Expense e WHERE e.paymentDate BETWEEN :from AND :to")
    Page<Expense> filterByPaymentDate(LocalDate from, LocalDate to, Pageable pageable);


    @Query(value = "SELECT e FROM Expense e WHERE e.dueDate BETWEEN :from AND :to")
    Page<Expense> filterByDueDate(LocalDate from, LocalDate to, Pageable pageable);

}
