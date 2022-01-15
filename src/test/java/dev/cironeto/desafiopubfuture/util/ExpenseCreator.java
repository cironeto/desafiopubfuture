package dev.cironeto.desafiopubfuture.util;

import dev.cironeto.desafiopubfuture.domain.Expense;
import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;

import java.time.LocalDate;

public class ExpenseCreator {

    public static Expense createExpense() {
        return Expense.builder()
                .id(1L)
                .value(200L)
                .paymentDate(LocalDate.parse("2022-01-10"))
                .dueDate(LocalDate.parse("2022-01-05"))
                .expenseType(ExpenseType.FOOD)
                .account(AccountCreator.createAccount())
                .build();
    }
}
