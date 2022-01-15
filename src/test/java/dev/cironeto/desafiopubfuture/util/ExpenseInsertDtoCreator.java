package dev.cironeto.desafiopubfuture.util;

import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import dev.cironeto.desafiopubfuture.dto.ExpenseInsertDto;

import java.time.LocalDate;

public class ExpenseInsertDtoCreator {

    public static ExpenseInsertDto createExpenseInsertDto() {
        return ExpenseInsertDto.builder()
                .id(1L)
                .value(200L)
                .paymentDate(LocalDate.parse("2022-01-10"))
                .dueDate(LocalDate.parse("2022-01-05"))
                .expenseType(ExpenseType.FOOD)
                .accountId(1L)
                .build();
    }
}
