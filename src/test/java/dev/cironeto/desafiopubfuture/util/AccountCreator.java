package dev.cironeto.desafiopubfuture.util;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.domain.enums.AccountType;

public class AccountCreator {

    public static Account createAccount() {
        return Account.builder()
                .id(10L)
                .balance(1000L)
                .accountType(AccountType.CHECKING_ACCOUNT)
                .financialInstitution("Banco Inter")
                .build();
    }

    public static Account createTargetAccountForTransfer() {
        return Account.builder()
                .id(20L)
                .balance(1000L)
                .accountType(AccountType.CHECKING_ACCOUNT)
                .financialInstitution("Banco Inter")
                .build();
    }
}
