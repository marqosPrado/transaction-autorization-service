package com.marcosprado.transactionautorizationservice.presentation.dto;

import com.marcosprado.transactionautorizationservice.domain.model.TransactionOperationType;
import com.marcosprado.transactionautorizationservice.domain.model.TransactionStatus;

import java.time.Instant;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        TransactionOperationType type,
        MoneyDTO amount,
        TransactionStatus status,
        Instant timestamp
) {

}
