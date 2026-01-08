package com.marcosprado.transactionautorizationservice.infrastructure.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatedAccountMessage(
        @JsonProperty("account")
        @NotNull
        AccountData account
) {
    public record AccountData(
            @NotBlank String id,
            @NotBlank String owner,
            @JsonProperty("created_at")
            @NotBlank String createdAt,
            @NotBlank String status
    ) {
    }
}
