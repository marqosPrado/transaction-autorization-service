package com.marcosprado.transactionautorizationservice.domain.exception;

public class InvalidOperationException extends DomainException {

    private static final String ERROR_CODE = "INVALID_OPERATION";

    public InvalidOperationException(String message) {
        super(message, ERROR_CODE);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}
