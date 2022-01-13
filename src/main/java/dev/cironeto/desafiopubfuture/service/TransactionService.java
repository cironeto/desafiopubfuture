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

import javax.persistence.EntityNotFoundException;

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
        Account sourceAccount = accountRepository.getById(requestBody.getSourceAccountId());
        Account targetAccount = accountRepository.getById(requestBody.getTargetAccountId());
        Long amount = requestBody.getAmount();

        validateTransfer(sourceAccount, targetAccount, amount);

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
    }


    private void validateTransfer(Account sourceAccount, Account targetAccount, Long amount) {
        log.info("Validating the transaction");
        try {
            if (sourceAccount.getBalance() <= 0 ||
                    sourceAccount.getBalance() < amount ||
                    sourceAccount.getId().equals(targetAccount.getId())) {
                throw new IllegalArgumentException("Can not transfer to the same account or insufficient balance");
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID not found");
        }
    }

}
