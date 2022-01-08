package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.util.GetTotalBalanceReturnBody;
import dev.cironeto.desafiopubfuture.util.TransferRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public GetTotalBalanceReturnBody getTotalBalance() {
        return new GetTotalBalanceReturnBody(accountRepository.getTotalBalance());
    }

    @Transactional
    public boolean makeTransfer(TransferRequestBody requestBody) {
        Account sourceAccount = accountRepository.getById(requestBody.getSourceAccountId());
        Account targetAccount = accountRepository.getById(requestBody.getTargetAccountId());
        Long amount = requestBody.getAmount();

        if (
                sourceAccount.getBalance() <= 0 ||
                sourceAccount.getBalance() < amount ||
                sourceAccount.equals(targetAccount)
        ) {
            throw new IllegalArgumentException();
        }
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        return true;
    }
}
