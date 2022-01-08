package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.domain.Expense;
import dev.cironeto.desafiopubfuture.domain.Income;
import dev.cironeto.desafiopubfuture.dto.AccountDto;
import dev.cironeto.desafiopubfuture.dto.ExpenseDto;
import dev.cironeto.desafiopubfuture.dto.IncomeDto;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.repository.ExpenseRepository;
import dev.cironeto.desafiopubfuture.repository.IncomeRepository;
import dev.cironeto.desafiopubfuture.service.exception.DatabaseException;
import dev.cironeto.desafiopubfuture.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional(readOnly = true)
    public Page<AccountDto> findAllPaged(Pageable pageable) {
        Page<Account> list = accountRepository.findAll(pageable);
        return list.map(AccountDto::new);
    }

    @Transactional
    public AccountDto save(AccountDto dto) {
        Account entity = new Account();
        copyDtoToEntity(dto, entity);
        entity = accountRepository.save(entity);
        return new AccountDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        try {
            accountRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("ID " + id + " not found");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation. Cannot delete this user");
        }
    }

    @Transactional
    public AccountDto update(Long id, AccountDto dto) {
        try {
            Account entity = accountRepository.getById(id);
            copyDtoToEntity(dto, entity);
            entity = accountRepository.save(entity);
            return new AccountDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID " + id + " not found");
        }
    }


    private void copyDtoToEntity(AccountDto dto, Account entity) {
        entity.setBalance(dto.getBalance());
        entity.setAccountType(dto.getAccountType());
        entity.setFinancialInstitution(dto.getFinancialInstitution());

        entity.getIncomes().clear();
        for (IncomeDto incomeDto : dto.getIncomes()) {
            Income income = incomeRepository.getById(incomeDto.getId());
            entity.getIncomes().add(income);
        }

        for (ExpenseDto expenseDto : dto.getExpenses()) {
            Expense expense = expenseRepository.getById(expenseDto.getId());
            entity.getExpenses().add(expense);
        }
    }
}
