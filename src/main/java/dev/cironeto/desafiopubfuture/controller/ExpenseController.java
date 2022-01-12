package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.domain.enums.ExpenseType;
import dev.cironeto.desafiopubfuture.dto.ExpenseDto;
import dev.cironeto.desafiopubfuture.dto.ExpenseInsertDto;
import dev.cironeto.desafiopubfuture.service.ExpenseService;
import dev.cironeto.desafiopubfuture.util.GetTotalExpensesReturnBody;
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
@RequestMapping(value = "/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<Page<ExpenseDto>> findAllPaged(Pageable pageable) {
        return ResponseEntity.ok(expenseService.findAllPaged(pageable));
    }

    @PostMapping
    public ResponseEntity<ExpenseInsertDto> save(@RequestBody ExpenseInsertDto dto) {
        return new ResponseEntity<>(expenseService.save(dto), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ExpenseInsertDto> update(@PathVariable Long id, @RequestBody ExpenseInsertDto dto) {
        return ResponseEntity.ok(expenseService.update(id, dto));
    }

    @GetMapping(value = "/total")
    public ResponseEntity<GetTotalExpensesReturnBody> getTotalExpenses(){
        return ResponseEntity.ok(expenseService.getTotalExpenses());
    }

    @GetMapping(value = "/{type}")
    public ResponseEntity<Page<ExpenseDto>> filterByType(Pageable pageable, @PathVariable ExpenseType type) {
        return ResponseEntity.ok(expenseService.filterByType(pageable, type));
    }

    @GetMapping(value = "/payment-date")
    public ResponseEntity<Page<ExpenseDto>> filterByPaymentDate
            (Pageable pageable,
             @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
             @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(expenseService.filterByPaymentDate(from, to, pageable));
    }

    @GetMapping(value = "/due-date")
    public ResponseEntity<Page<ExpenseDto>> filterByDueDate
            (Pageable pageable,
             @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
             @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(expenseService.filterByDueDate(from, to, pageable));
    }
}
