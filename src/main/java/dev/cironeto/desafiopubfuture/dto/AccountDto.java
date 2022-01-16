package dev.cironeto.desafiopubfuture.dto;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.domain.enums.AccountType;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountDto implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;
    private Long balance;
    private AccountType accountType;
    private String financialInstitution;

    public AccountDto(Account entity) {
        id = entity.getId();
        balance = entity.getBalance();
        accountType = entity.getAccountType();
        financialInstitution = entity.getFinancialInstitution();
    }
}
