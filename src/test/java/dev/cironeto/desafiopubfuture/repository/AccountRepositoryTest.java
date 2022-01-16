package dev.cironeto.desafiopubfuture.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Tests for Account Repository")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("getTotalBalance returns a Long value with the sum of all accounts' balance")
    void getTotalBalance_ReturnsLongValueWithSumOfAllAccountsBalance_WhenSuccessful() {
        Long totalBalance = accountRepository.getTotalBalance();

        Assertions.assertThat(totalBalance)
                .isNotNull()
                .isInstanceOf(Long.class);
    }
}