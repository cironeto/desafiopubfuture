package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.domain.enums.IncomeType;
import dev.cironeto.desafiopubfuture.dto.IncomeDto;
import dev.cironeto.desafiopubfuture.dto.IncomeInsertDto;
import dev.cironeto.desafiopubfuture.service.IncomeService;
import dev.cironeto.desafiopubfuture.util.GetTotalIncomesReturnBody;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/incomes")
public class IncomeController {
    private final IncomeService incomeService;

    @GetMapping
    public ResponseEntity<Page<IncomeDto>> findAllPaged(Pageable pageable) {
        return ResponseEntity.ok(incomeService.findAllPaged(pageable));
    }

    @PostMapping
    public ResponseEntity<IncomeInsertDto> save(@RequestBody IncomeInsertDto dto) {
        return new ResponseEntity<>(incomeService.save(dto), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incomeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<IncomeInsertDto> update(@PathVariable Long id, @RequestBody IncomeInsertDto dto) {
        return ResponseEntity.ok(incomeService.update(id, dto));
    }

    @GetMapping(value = "/total")
    public ResponseEntity<GetTotalIncomesReturnBody> getTotalIncomes() {
        return ResponseEntity.ok(incomeService.getTotalIncomes());
    }

    @GetMapping(value = "/{type}")
    public ResponseEntity<Page<IncomeDto>> filterByType(Pageable pageable, @PathVariable IncomeType type) {
        return ResponseEntity.ok(incomeService.filterByType(pageable, type));
    }

    @GetMapping(value = "/receiving-date")
    public ResponseEntity<Page<IncomeDto>> filterByReceivingDate
            (Pageable pageable,
             @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
             @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(incomeService.filterByReceivingDate(from, to, pageable));
    }

    @GetMapping(value = "/expected-date")
    public ResponseEntity<Page<IncomeDto>> filterByExpectedReceivingDate
            (Pageable pageable,
             @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
             @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(incomeService.filterByExpectedReceivingDate(from, to, pageable));
    }
}
