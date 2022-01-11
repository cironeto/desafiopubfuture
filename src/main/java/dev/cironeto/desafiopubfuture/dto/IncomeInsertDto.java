package dev.cironeto.desafiopubfuture.dto;

import dev.cironeto.desafiopubfuture.domain.Income;
import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IncomeInsertDto implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;
    private Long value;
    private LocalDate receivingDate;
    private LocalDate expectedReceivingDate;
    private String description;
    private IncomeType incomeType;
    private Long accountId;

    public IncomeInsertDto (Income entity) {
        id = entity.getId();
        value = entity.getValue();
        receivingDate = entity.getReceivingDate();
        expectedReceivingDate = entity.getExpectedReceivingDate();
        description = entity.getDescription();
        incomeType = entity.getIncomeType();
        accountId = entity.getAccount().getId();
    }
}
