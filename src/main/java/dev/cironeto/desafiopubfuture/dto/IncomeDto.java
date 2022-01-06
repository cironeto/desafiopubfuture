package dev.cironeto.desafiopubfuture.dto;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.domain.Income;
import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IncomeDto implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;
    private Long value;
    private Instant receivingDate;
    private Instant expectedReceivingDate;
    private String description;
    private IncomeType incomeType;
    private Account account;

    public IncomeDto (Income entity) {
        id = entity.getId();
        value = entity.getValue();
        receivingDate = entity.getReceivingDate();
        expectedReceivingDate = entity.getExpectedReceivingDate();
        description = entity.getDescription();
        incomeType = entity.getIncomeType();
        account = entity.getAccount();
    }
}