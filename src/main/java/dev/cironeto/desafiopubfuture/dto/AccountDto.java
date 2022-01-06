package dev.cironeto.desafiopubfuture.dto;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.domain.enums.AccountType;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountDto implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;
    private Long balance;
    private AccountType accountType;
    private String financialInstitution;
    private List<ExpenseDto> expenses = new ArrayList<>();
    private List<IncomeDto> incomes = new ArrayList<>();

    public AccountDto (Account entity) {
        id = entity.getId();
        balance = entity.getBalance();
        accountType = entity.getAccountType();
        financialInstitution = entity.getFinancialInstitution();
        entity.getExpenses().forEach(ex -> expenses.add(new ExpenseDto(ex)));
        entity.getIncomes().forEach(in -> incomes.add(new IncomeDto(in)));
    }
}
