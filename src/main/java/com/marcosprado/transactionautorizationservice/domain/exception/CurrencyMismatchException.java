package com.marcosprado.transactionautorizationservice.domain.exception;

public class CurrencyMismatchException extends DomainException {
    private static final String ERROR_CODE = "CURRENCY_MISMATCH";

    private final String requestCurrency;
    private final String accountCurrency;

    public CurrencyMismatchException(String requestCurrency, String accountCurrency) {
        super(String.format("Currency error. Request: %s, Account: %s",
                requestCurrency, accountCurrency), ERROR_CODE);
        this.requestCurrency = requestCurrency;
        this.accountCurrency = accountCurrency;
    }

    public String getRequestCurrency() {
        return requestCurrency;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }
}
