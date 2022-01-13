package dev.cironeto.desafiopubfuture.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TransferRequestBody {

    private Long sourceAccountId;
    private Long targetAccountId;
    private Long amount;
}
