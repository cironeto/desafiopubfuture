package dev.cironeto.desafiopubfuture.repository;

import dev.cironeto.desafiopubfuture.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
