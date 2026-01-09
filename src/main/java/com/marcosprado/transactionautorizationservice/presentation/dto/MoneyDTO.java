package com.marcosprado.transactionautorizationservice.presentation.dto;

import java.math.BigDecimal;

public record MoneyDTO(
        BigDecimal value,
        String currency
) {
}
