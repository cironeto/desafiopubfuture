package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.service.TransactionService;
import dev.cironeto.desafiopubfuture.util.GetTotalBalanceReturnBody;
import dev.cironeto.desafiopubfuture.util.TransferRequestBody;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping(value = "/total-balance")
    public ResponseEntity<GetTotalBalanceReturnBody> getTotalBalance() {
        return ResponseEntity.ok(transactionService.getTotalBalance());
    }

    @PutMapping(value = "/transfer")
    public ResponseEntity<Void> makeTransfer(@RequestBody TransferRequestBody requestBody) {
        transactionService.makeTransfer(requestBody);
        return ResponseEntity.noContent().build();
    }


}
