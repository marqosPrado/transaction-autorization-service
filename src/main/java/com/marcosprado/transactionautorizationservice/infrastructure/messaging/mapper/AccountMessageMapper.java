package com.marcosprado.transactionautorizationservice.infrastructure.messaging.mapper;

import com.marcosprado.transactionautorizationservice.application.command.CreateAccountCommand;
import com.marcosprado.transactionautorizationservice.domain.model.AccountStatus;
import com.marcosprado.transactionautorizationservice.infrastructure.messaging.dto.CreatedAccountMessage;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class AccountMessageMapper {

    public CreateAccountCommand toCommand(CreatedAccountMessage message) {
        try {
            return new CreateAccountCommand(
                    UUID.fromString(message.account().id()),
                    UUID.fromString(message.account().owner()),
                    AccountStatus.valueOf(message.account().status()),
                    Instant.ofEpochSecond(Long.parseLong(message.account().createdAt()))
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid account message format: " + message, e);
        }
    }
}
