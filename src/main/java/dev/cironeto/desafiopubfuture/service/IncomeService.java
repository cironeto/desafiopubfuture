package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Income;
import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import dev.cironeto.desafiopubfuture.dto.IncomeDto;
import dev.cironeto.desafiopubfuture.dto.IncomeInsertDto;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.repository.IncomeRepository;
import dev.cironeto.desafiopubfuture.service.exception.DatabaseException;
import dev.cironeto.desafiopubfuture.service.exception.ResourceNotFoundException;
import dev.cironeto.desafiopubfuture.util.GetTotalIncomesReturnBody;
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
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Page<IncomeDto> findAllPaged(Pageable pageable) {
        Page<Income> list = incomeRepository.findAll(pageable);
        return list.map(IncomeDto::new);
    }

    @Transactional
    public IncomeInsertDto save(IncomeInsertDto dto) {
        try {
            Income entity = new Income();
            copyDtoToEntity(dto, entity);
            entity = incomeRepository.save(entity);

            return new IncomeInsertDto(entity);

        } catch (DataIntegrityViolationException e) {
            log.info("Account ID not found when trying to save the income");
            throw new DatabaseException("Account ID not found");
        }
    }

    @Transactional
    public IncomeInsertDto update(Long id, IncomeInsertDto dto) {
        try {
            Income entity = incomeRepository.getById(id);
            copyDtoToEntity(dto, entity);
            entity = incomeRepository.save(entity);
            incomeRepository.flush();

            return new IncomeInsertDto(entity);

        } catch (DataIntegrityViolationException e) {
            log.info("Account ID not found when trying to update the income");
            throw new DatabaseException("Account ID not found");

        } catch (EntityNotFoundException e) {
            log.info("Income ID " + id + " not found when trying to update");
            throw new ResourceNotFoundException("Income ID " + id + " not found");
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            incomeRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            log.info("ID " + id + " not found when trying to delete");
            throw new ResourceNotFoundException("ID " + id + " not found");

        } catch (DataIntegrityViolationException e) {
            log.info("Integrity violation. Cannot delete this income");
            throw new DatabaseException("Integrity violation. Cannot delete this income");
        }
    }

    @Transactional(readOnly = true)
    public GetTotalIncomesReturnBody getTotalIncomes() {
        return new GetTotalIncomesReturnBody(incomeRepository.getTotalIncomes());
    }

    @Transactional(readOnly = true)
    public Page<IncomeDto> filterByType(Pageable pageable, IncomeType type) {
        Page<Income> list = incomeRepository.findByIncomeType(type, pageable);
        return list.map(IncomeDto::new);
    }

    @Transactional(readOnly = true)
    public Page<IncomeDto> filterByReceivingDate(LocalDate from, LocalDate to, Pageable pageable) {
        Page<Income> list = incomeRepository.filterByReceivingDate(from, to, pageable);
        return list.map(IncomeDto::new);
    }

    @Transactional(readOnly = true)
    public Page<IncomeDto> filterByExpectedReceivingDate(LocalDate from, LocalDate to, Pageable pageable) {
        Page<Income> list = incomeRepository.filterByExpectedReceivingDate(from, to, pageable);
        return list.map(IncomeDto::new);
    }


    private void copyDtoToEntity(IncomeInsertDto dto, Income entity) {
        entity.setValue(dto.getValue());
        entity.setReceivingDate(dto.getReceivingDate());
        entity.setExpectedReceivingDate(dto.getExpectedReceivingDate());
        entity.setDescription(dto.getDescription());
        entity.setIncomeType(dto.getIncomeType());
        entity.setAccount(accountRepository.getById(dto.getAccountId()));
    }
}

