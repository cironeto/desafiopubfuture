package dev.cironeto.desafiopubfuture.domain;

import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tb_expense")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Expense implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private Long value;
    private LocalDate paymentDate;
    private LocalDate dueDate;
    private ExpenseType expenseType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
