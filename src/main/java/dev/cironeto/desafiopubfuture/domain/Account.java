package dev.cironeto.desafiopubfuture.domain;

import dev.cironeto.desafiopubfuture.domain.enums.AccountType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private Long balance;
    private AccountType accountType;
    private String financialInstitution;

    @OneToMany(mappedBy = "account")
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Income> incomes = new ArrayList<>();
}
