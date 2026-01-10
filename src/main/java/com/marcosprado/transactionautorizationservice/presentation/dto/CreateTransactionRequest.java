package com.marcosprado.transactionautorizationservice.presentation.dto;

import com.marcosprado.transactionautorizationservice.domain.model.TransactionOperationType;
import com.marcosprado.transactionautorizationservice.presentation.validation.ValidCurrency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateTransactionRequest(
        @NotNull(message = "Account ID is required")
        UUID accountId,

        @NotNull(message = "Operation type is required")
        TransactionOperationType operationType,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        String value,

        @NotNull(message = "Currency is required")
        @ValidCurrency
        String currency
) {
}
