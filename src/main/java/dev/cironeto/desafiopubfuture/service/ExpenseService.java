package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Expense;
import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import dev.cironeto.desafiopubfuture.dto.ExpenseDto;
import dev.cironeto.desafiopubfuture.dto.ExpenseInsertDto;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.repository.ExpenseRepository;
import dev.cironeto.desafiopubfuture.service.exception.DatabaseException;
import dev.cironeto.desafiopubfuture.service.exception.ResourceNotFoundException;
import dev.cironeto.desafiopubfuture.util.GetTotalExpensesReturnBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Page<ExpenseDto> findAllPaged(Pageable pageable) {
        Page<Expense> list = expenseRepository.findAll(pageable);
        return list.map(ExpenseDto::new);
    }

    @Transactional
    public ExpenseInsertDto save(ExpenseInsertDto dto) {
        try {
            Expense entity = new Expense();
            copyDtoToEntity(dto, entity);
            entity = expenseRepository.save(entity);

            return new ExpenseInsertDto(entity);

        } catch (DataIntegrityViolationException e) {
            log.info("Account ID not found when trying to save the expense");
            throw new DatabaseException("Account ID not found");
        }
    }

    @Transactional
    public ExpenseInsertDto update(Long id, ExpenseInsertDto dto) {
        try {
            Expense entity = expenseRepository.getById(id);
            copyDtoToEntity(dto, entity);
            entity = expenseRepository.save(entity);
            expenseRepository.flush();

            return new ExpenseInsertDto(entity);

        } catch (DataIntegrityViolationException e) {
            log.info("Account ID not found when trying to update the expense");
            throw new DatabaseException("Account ID not found");

        } catch (EntityNotFoundException e) {
            log.info("Expense ID " + id + " not found when trying to update");
            throw new ResourceNotFoundException("Expense ID " + id + " not found");
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            expenseRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            log.info("ID " + id + " not found when trying to delete");
            throw new ResourceNotFoundException("ID " + id + " not found");

        } catch (DataIntegrityViolationException e) {
            log.info("Integrity violation. Cannot delete this expense");
            throw new DatabaseException("Integrity violation. Cannot delete this expense");
        }
    }

    @Transactional(readOnly = true)
    public GetTotalExpensesReturnBody getTotalExpenses() {
        return new GetTotalExpensesReturnBody(expenseRepository.getTotalExpenses());
    }

    @Transactional(readOnly = true)
    public Page<ExpenseDto> filterByType(Pageable pageable, ExpenseType type) {
        Page<Expense> list = expenseRepository.findByExpenseType(type, pageable);
        return list.map(ExpenseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<ExpenseDto> filterByPaymentDate(LocalDate from, LocalDate to, Pageable pageable) {
        Page<Expense> list = expenseRepository.filterByPaymentDate(from, to, pageable);
        return list.map(ExpenseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<ExpenseDto> filterByDueDate(LocalDate from, LocalDate to, Pageable pageable) {
        Page<Expense> list = expenseRepository.filterByDueDate(from, to, pageable);
        return list.map(ExpenseDto::new);
    }


    private void copyDtoToEntity(ExpenseInsertDto dto, Expense entity) {
        entity.setValue(dto.getValue());
        entity.setValue(dto.getValue());
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setDueDate(dto.getDueDate());
        entity.setExpenseType(dto.getExpenseType());
        entity.setAccount(accountRepository.getById(dto.getAccountId()));
    }
}

