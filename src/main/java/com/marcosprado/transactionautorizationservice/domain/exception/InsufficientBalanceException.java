package com.marcosprado.transactionautorizationservice.domain.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends DomainException {

    private static final String ERROR_CODE = "INSUFFICIENT_BALANCE";

    private final BigDecimal currentBalance;
    private final BigDecimal requestedAmount;

    public InsufficientBalanceException(BigDecimal currentBalance, BigDecimal requestedAmount) {
        super(String.format("Insufficient balance. Current: %s, Requested: %s",
                currentBalance, requestedAmount), ERROR_CODE);
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
}
