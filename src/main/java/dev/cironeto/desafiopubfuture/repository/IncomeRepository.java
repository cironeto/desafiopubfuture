package dev.cironeto.desafiopubfuture.repository;

import dev.cironeto.desafiopubfuture.domain.Income;
import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    Page<Income> findByIncomeType(IncomeType incomeType, Pageable pageable);

    @Query(value = "SELECT SUM(value) FROM Income")
    Long getTotalIncomes();

    @Query(value = "SELECT i FROM Income i WHERE i.receivingDate BETWEEN :from AND :to")
    Page<Income> filterByReceivingDate(LocalDate from, LocalDate to, Pageable pageable);


    @Query(value = "SELECT i FROM Income i WHERE i.expectedReceivingDate BETWEEN :from AND :to")
    Page<Income> filterByExpectedReceivingDate(LocalDate from, LocalDate to, Pageable pageable);

}
