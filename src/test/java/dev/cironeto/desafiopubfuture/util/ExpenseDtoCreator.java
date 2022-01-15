package dev.cironeto.desafiopubfuture.util;

import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import dev.cironeto.desafiopubfuture.dto.ExpenseDto;

import java.time.LocalDate;

public class ExpenseDtoCreator {

    public static ExpenseDto createExpenseDto() {
        return ExpenseDto.builder()
                .id(1L)
                .value(200L)
                .paymentDate(LocalDate.parse("2022-01-10"))
                .dueDate(LocalDate.parse("2022-01-05"))
                .expenseType(ExpenseType.FOOD)
                .account(AccountCreator.createAccount())
                .build();
    }
}
