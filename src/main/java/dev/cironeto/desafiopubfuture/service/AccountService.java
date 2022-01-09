package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.dto.AccountDto;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.service.exception.DatabaseException;
import dev.cironeto.desafiopubfuture.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

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
            accountRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            log.info("ID " + id + " not found when trying to delete");
            throw new ResourceNotFoundException("ID " + id + " not found");
        } catch (DataIntegrityViolationException e) {
            log.info("Integrity violation. Cannot delete this account");
            throw new DatabaseException("Integrity violation. Cannot delete this account because it has related incomes/expenses");
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
            log.info("ID " + id + " not found when trying to update");
            throw new ResourceNotFoundException("ID " + id + " not found");
        }
    }


    private void copyDtoToEntity(AccountDto dto, Account entity) {
        entity.setBalance(dto.getBalance());
        entity.setAccountType(dto.getAccountType());
        entity.setFinancialInstitution(dto.getFinancialInstitution());
        }
    }
