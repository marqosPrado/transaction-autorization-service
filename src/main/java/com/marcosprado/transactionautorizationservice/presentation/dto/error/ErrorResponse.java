package com.marcosprado.transactionautorizationservice.presentation.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String message,
        String errorCode,
        String path,
        Map<String, Object> details
) {
    public ErrorResponse(int status, String message, String errorCode, String path) {
        this(Instant.now(), status, message, errorCode, path, null);
    }

    public ErrorResponse(int status, String message, String errorCode, String path, Map<String, Object> details) {
        this(Instant.now(), status, message, errorCode, path, details);
    }
}
