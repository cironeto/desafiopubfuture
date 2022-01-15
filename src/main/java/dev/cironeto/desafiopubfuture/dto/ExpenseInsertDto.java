package dev.cironeto.desafiopubfuture.dto;

import dev.cironeto.desafiopubfuture.domain.Expense;
import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExpenseInsertDto implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;
    private Long value;
    private LocalDate paymentDate;
    private LocalDate dueDate;
    private ExpenseType expenseType;
    private Long accountId;

    public ExpenseInsertDto(Expense entity) {
        id = entity.getId();
        value = entity.getValue();
        paymentDate = entity.getPaymentDate();
        dueDate = entity.getDueDate();
        expenseType = entity.getExpenseType();
        accountId = entity.getAccount().getId();
    }
}
