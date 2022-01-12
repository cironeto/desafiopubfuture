package dev.cironeto.desafiopubfuture.repository;

import dev.cironeto.desafiopubfuture.domain.Expense;
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

    @Query
            (
                    nativeQuery = true,
                    value = "SELECT SUM(value) FROM tb_expense;"
            )
    Long getTotalExpenses();

    Page<Expense> findByExpenseType(ExpenseType expenseType, Pageable pageable);

    @Query
            (
                    nativeQuery = true,
                    value = "SELECT * FROM tb_expense WHERE payment_date BETWEEN ?1 AND ?2"
            )
    Page<Expense> filterByPaymentDate(LocalDate from, LocalDate to, Pageable pageable);


    @Query
            (
                    nativeQuery = true,
                    value = "SELECT * FROM tb_expense WHERE due_date BETWEEN ?1 AND ?2"
            )
    Page<Expense> filterByDueDate(LocalDate from, LocalDate to, Pageable pageable);
    
}
