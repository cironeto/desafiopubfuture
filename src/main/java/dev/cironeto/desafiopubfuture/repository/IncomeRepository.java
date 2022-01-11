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

    @Query
            (
                nativeQuery = true,
                value = "SELECT SUM(value) FROM tb_income;"
            )
    Long getTotalIncomes();

    Page<Income> findByIncomeType(IncomeType incomeType, Pageable pageable);

    @Query
            (
                nativeQuery = true,
                value = "SELECT * FROM TB_INCOME WHERE RECEIVING_DATE BETWEEN ?1 AND ?2"
            )
    Page<Income> findByDateBetween(LocalDate from, LocalDate to, Pageable pageable);

}
