package com.marcosprado.transactionautorizationservice.presentation.dto;

public record TransactionResponse(
        TransactionDTO transaction,
        AccountBalanceDTO account
) {
}
