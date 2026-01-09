package com.marcosprado.transactionautorizationservice.presentation.dto;

import com.marcosprado.transactionautorizationservice.domain.model.TransactionOperationType;
import com.marcosprado.transactionautorizationservice.domain.model.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        TransactionOperationType type,
        MoneyDTO amount,
        TransactionStatus status,
        LocalDateTime timestamp
) {

}
