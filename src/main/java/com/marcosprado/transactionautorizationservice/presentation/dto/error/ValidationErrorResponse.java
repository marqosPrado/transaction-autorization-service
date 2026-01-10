package com.marcosprado.transactionautorizationservice.presentation.dto.error;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        Instant timestamp,
        int status,
        String message,
        String errorCode,
        String path,
        List<FieldError> fieldErrors
) {
    public ValidationErrorResponse(int status, String message, String path, List<FieldError> fieldErrors) {
        this(Instant.now(), status, message, "VALIDATION_ERROR", path, fieldErrors);
    }
}
