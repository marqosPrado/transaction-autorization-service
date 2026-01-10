package com.marcosprado.transactionautorizationservice.presentation.dto.error;

public record FieldError(
        String field,
        Object rejectedValue,
        String message
) {
}
