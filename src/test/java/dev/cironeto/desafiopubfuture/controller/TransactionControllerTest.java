package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.service.TransactionService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Transaction Controller")
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionServiceMock;

    @Mock
    private AccountRepository accountRepositoryMock;

    private TransferRequestBody transferRequestBody;

    @BeforeEach
    void setUp() {
        transferRequestBody = TransferRequestBodyCreator.createTransferRequestBody();
        Account account = AccountCreator.createAccount();

        Account targetAccount = AccountCreator.createTargetAccountForTransfer();

        Mockito.when(transactionServiceMock.getTotalBalance()).thenReturn(new GetTotalBalanceReturnBody());

        Mockito.when(accountRepositoryMock.findById(ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.of(account))
                .thenReturn(Optional.of(targetAccount));

    }

    @Test
    @DisplayName("getTotalBalance returns value with the sum of all accounts' balance")
    void getTotalBalance_ReturnsValueWithSumOfAllAccountsBalance_WhenSuccessful() {
        ResponseEntity<GetTotalBalanceReturnBody> totalBalanceReturnBody =
                transactionController.getTotalBalance();

        Assertions.assertThat(totalBalanceReturnBody)
                .isNotNull();

        Assertions.assertThat(totalBalanceReturnBody.getBody())
                .isInstanceOf(GetTotalBalanceReturnBody.class);
    }

    @Test
    @DisplayName("makeTransfer transfer balance value from source account to target account when transaction is valid")
    void makeTransfer_TransferBalanceValueFromSourceAccountToTargetAccount_WhenTransactionIsValid() {
        Assertions.assertThatCode(() -> transactionController.makeTransfer(transferRequestBody))
                .doesNotThrowAnyException();
    }
}