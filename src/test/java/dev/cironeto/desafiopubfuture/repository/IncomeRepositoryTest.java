package dev.cironeto.desafiopubfuture.repository;

import dev.cironeto.desafiopubfuture.domain.Income;
import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@DataJpaTest
@DisplayName("Tests for Income Repository")
class IncomeRepositoryTest {

    @Autowired
    private IncomeRepository incomeRepository;

    private Pageable pageable;
    private IncomeType incomeType;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        incomeType = IncomeType.SALARY;
        dateFrom = LocalDate.parse("2021-06-13");
        dateTo = LocalDate.parse("2021-09-15");
    }

    @Test
    @DisplayName("findByIncomeType returns an income list filtered by the income type")
    void findByIncomeType_ReturnsIncomeListFilteredByType_WhenSuccessful() {
        Page<Income> page = incomeRepository.findByIncomeType(incomeType, pageable);

        Assertions.assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("getTotalIncomes returns a Long value of the sum of all incomes")
    void getTotalIncomes_ReturnsLongValue_WhenSuccessful() {
        Long totalIncomes = incomeRepository.getTotalIncomes();

        Assertions.assertThat(totalIncomes)
                .isNotNull()
                .isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("filterByReceivingDate returns an income list filtered by receiving date between two dates")
    void filterByReceivingDate_ReturnIncomeListFilteredByReceivingDate_WhenDatesValid() {
        Page<Income> page = incomeRepository.filterByReceivingDate(dateFrom, dateTo, pageable);

        Assertions.assertThat(page).isNotNull();
    }


    @Test
    @DisplayName("filterByExpectedReceivingDate returns an income list filtered by expected receiving date between two dates")
    void filterByExpectedReceivingDate_ReturnIncomeListFilteredByExpectedReceivingDate_WhenDatesValid() {
        Page<Income> page = incomeRepository.filterByExpectedReceivingDate(dateFrom, dateTo, pageable);

        Assertions.assertThat(page).isNotNull();
    }
}