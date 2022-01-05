package dev.cironeto.desafiopubfuture.domain;

import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Income implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private Long value;
    private Instant receivingDate;
    private Instant expectedReceivingDate;
    private String description;
    private IncomeType incomeType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
