package dev.cironeto.desafiopubfuture.domain;

import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "tb_expense")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Expense implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private Long value;
    private Instant paymentDate;
    private Instant dueDate;
    private ExpenseType expenseType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
