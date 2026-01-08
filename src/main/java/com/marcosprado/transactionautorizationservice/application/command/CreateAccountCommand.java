package com.marcosprado.transactionautorizationservice.application.command;

import com.marcosprado.transactionautorizationservice.domain.model.AccountStatus;

import java.time.Instant;
import java.util.UUID;

public record CreateAccountCommand(
        UUID id,
        UUID ownerId,
        AccountStatus status,
        Instant createdAt
) {
}
