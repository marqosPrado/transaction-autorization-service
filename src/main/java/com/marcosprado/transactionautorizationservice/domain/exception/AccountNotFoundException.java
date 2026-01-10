package com.marcosprado.transactionautorizationservice.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends DomainException {

    private static final String ERROR_CODE = "ACCOUNT_NOT_FOUND";

    public AccountNotFoundException(UUID accountId) {
        super(String.format("Account with ID '%s' not found", accountId), ERROR_CODE);
    }

    public AccountNotFoundException(String message) {
        super(message, ERROR_CODE);
    }
}
