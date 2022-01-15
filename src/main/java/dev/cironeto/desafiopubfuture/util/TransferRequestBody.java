package dev.cironeto.desafiopubfuture.util;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TransferRequestBody {

    private Long sourceAccountId;
    private Long targetAccountId;
    private Long amount;
}
