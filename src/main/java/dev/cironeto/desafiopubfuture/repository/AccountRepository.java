package dev.cironeto.desafiopubfuture.repository;

import dev.cironeto.desafiopubfuture.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT SUM(balance) FROM Account")
    Long getTotalBalance();
}
