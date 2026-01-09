package com.marcosprado.transactionautorizationservice.presentation.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountBalanceDTO(
        UUID id,
        BalanceDTO balance
) {
    public record BalanceDTO(
            BigDecimal amount,
            String currency
    ) {
    }
}
