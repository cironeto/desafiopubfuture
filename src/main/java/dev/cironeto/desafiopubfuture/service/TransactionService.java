package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.service.exception.ResourceNotFoundException;
import dev.cironeto.desafiopubfuture.util.GetTotalBalanceReturnBody;
import dev.cironeto.desafiopubfuture.util.TransferRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public GetTotalBalanceReturnBody getTotalBalance() {
        return new GetTotalBalanceReturnBody(accountRepository.getTotalBalance());
    }

    @Transactional
    public void makeTransfer(TransferRequestBody requestBody) {
        Optional<Account> sourceAccountOpt = accountRepository.findById(requestBody.getSourceAccountId());
        Optional<Account> targetAccountOpt = accountRepository.findById(requestBody.getTargetAccountId());
        Long amount = requestBody.getAmount();

        Account sourceAccount = sourceAccountOpt.orElseThrow(() -> new ResourceNotFoundException("ID not found"));
        Account targetAccount = targetAccountOpt.orElseThrow(() -> new ResourceNotFoundException("ID not found"));

        validateTransfer(sourceAccount, targetAccount, amount);

        subtractAmountFromSourceAccountAndAddToTargetAmount
                (sourceAccount, targetAccount, amount);
    }


    private void subtractAmountFromSourceAccountAndAddToTargetAmount
            (Account sourceAccount, Account targetAccount, Long amount) {
        log.info("Making the transfer and changing the balance of involved accounts");
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

    }

    private void validateTransfer(Account sourceAccount, Account targetAccount, Long amount) {
        log.info("Validating the transaction");
        if (sourceAccount.getBalance() <= 0 ||
                sourceAccount.getBalance() < amount ||
                sourceAccount.getId().equals(targetAccount.getId())) {
            throw new IllegalArgumentException("Can not transfer to the same account or insufficient balance");
        }
    }
}
