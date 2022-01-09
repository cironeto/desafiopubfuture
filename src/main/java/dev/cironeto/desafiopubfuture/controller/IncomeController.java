package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.dto.IncomeDto;
import dev.cironeto.desafiopubfuture.dto.IncomeInsertDto;
import dev.cironeto.desafiopubfuture.service.IncomeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

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
    public ResponseEntity<IncomeInsertDto> update(@PathVariable Long id, @RequestBody IncomeInsertDto dto) throws SQLException {
        return ResponseEntity.ok(incomeService.update(id, dto));
    }
}
