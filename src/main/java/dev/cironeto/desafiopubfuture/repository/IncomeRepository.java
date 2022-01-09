package dev.cironeto.desafiopubfuture.repository;

import dev.cironeto.desafiopubfuture.domain.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query
    (
        nativeQuery = true,
        value = "SELECT SUM(value) FROM tb_income;"
    )
    Long getTotalIncomes();
}
