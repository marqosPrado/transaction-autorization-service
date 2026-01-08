package com.marcosprado.transactionautorizationservice.application.command;

import com.marcosprado.transactionautorizationservice.domain.model.TransactionOperationType;

import java.util.UUID;

public record CreateTransactionCommand(
        UUID accountId,
        TransactionOperationType operationType,
        Long amountCents,
        String currency
) {
}
