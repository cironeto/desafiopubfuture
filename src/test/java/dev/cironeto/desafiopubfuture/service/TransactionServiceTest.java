package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.util.AccountCreator;
import dev.cironeto.desafiopubfuture.util.GetTotalBalanceReturnBody;
import dev.cironeto.desafiopubfuture.util.TransferRequestBody;
import dev.cironeto.desafiopubfuture.util.TransferRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Transaction Service")
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepositoryMock;

    private Account account;
    private TransferRequestBody transferRequestBody;

    @BeforeEach
    void setUp() {
        transferRequestBody = TransferRequestBodyCreator.createTransferRequestBody();
        account = AccountCreator.createAccount();

        Long totalBalance = 100L;
        Account targetAccount = AccountCreator.createTargetAccountForTransfer();

        Mockito.when(accountRepositoryMock.getTotalBalance()).thenReturn(totalBalance);

        Mockito.when(accountRepositoryMock.findById(ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.of(account))
                .thenReturn(Optional.of(targetAccount));

    }

    @Test
    @DisplayName("returns value of the sum of all accounts' balance")
    void getTotalBalance_ReturnsLongValueWithSumOfAllAccountsBalance_WhenSuccessful() {
        GetTotalBalanceReturnBody totalBalanceReturnBody = transactionService.getTotalBalance();

        Assertions.assertThat(totalBalanceReturnBody)
                .isNotNull()
                .isInstanceOf(GetTotalBalanceReturnBody.class);
    }

    @Test
    @DisplayName("makeTransfer transfer balance value from source account to target account when transaction is valid")
    void makeTransfer_TransferBalanceValueFromSourceAccountToTargetAccount_WhenTransactionIsValid() {
        Assertions.assertThatCode(() -> transactionService.makeTransfer(transferRequestBody))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("makeTransfer throws IllegalArgumentException when transaction is not valid")
    void makeTransfer_ThrowsIllegalArgumentException_WhenTransactionIsNotValid() {

        Mockito.when(accountRepositoryMock.findById(ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.of(account));

        Assertions.assertThatThrownBy(() ->  transactionService.makeTransfer(transferRequestBody))
                .isInstanceOf(IllegalArgumentException.class);
    }
}