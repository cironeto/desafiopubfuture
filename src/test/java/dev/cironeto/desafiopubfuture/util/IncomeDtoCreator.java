package dev.cironeto.desafiopubfuture.util;

import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import dev.cironeto.desafiopubfuture.dto.IncomeDto;

import java.time.LocalDate;

public class IncomeDtoCreator {

    public static IncomeDto createIncomeDto() {
        return IncomeDto.builder()
                .id(1L)
                .value(200L)
                .receivingDate(LocalDate.parse("2022-01-10"))
                .expectedReceivingDate(LocalDate.parse("2022-01-05"))
                .description("Ref 01/2022")
                .incomeType(IncomeType.BONUS)
                .account(AccountCreator.createAccount())
                .build();
    }
}
