package dev.cironeto.desafiopubfuture.util;


public class TransferRequestBodyCreator {

    public static TransferRequestBody createTransferRequestBody() {
        return TransferRequestBody.builder()
                .sourceAccountId(1L)
                .targetAccountId(2L)
                .amount(0L)
                .build();
    }

}
