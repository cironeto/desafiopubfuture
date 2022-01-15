package dev.cironeto.desafiopubfuture.util;

import dev.cironeto.desafiopubfuture.domain.enums.AccountType;
import dev.cironeto.desafiopubfuture.dto.AccountDto;

public class AccountDtoCreator {

    public static AccountDto createAccountDto() {
        return AccountDto.builder()
                .id(10L)
                .balance(1000l)
                .accountType(AccountType.CHECKING_ACCOUNT)
                .financialInstitution("Banco Inter")
                .build();
    }

}
