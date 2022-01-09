package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.dto.AccountDto;
import dev.cironeto.desafiopubfuture.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/accounts")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Page<AccountDto>> findAllPaged(Pageable pageable) {
        return ResponseEntity.ok(accountService.findAllPaged(pageable));
    }

    @PostMapping
    public ResponseEntity<AccountDto> save(@RequestBody AccountDto dto) {
        return new ResponseEntity<>(accountService.save(dto), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto dto) {
        return ResponseEntity.ok(accountService.update(id, dto));
    }
}
