package com.marcosprado.transactionautorizationservice.domain.model;

import com.marcosprado.transactionautorizationservice.domain.exception.InsufficientBalanceException;
import com.marcosprado.transactionautorizationservice.domain.exception.InvalidOperationException;
import com.marcosprado.transactionautorizationservice.domain.util.MoneyConverter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Account {
    private UUID id;
    private UUID ownerId;
    private AccountStatus status;
    private Long amountCents;
    private String currency;
    private Instant createdAt;

    public Account(UUID id, UUID ownerId, AccountStatus status, Instant createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;
        this.amountCents = 0L;
        this.currency = "BRL";
        this.createdAt = createdAt;
    }

    public Account(UUID id, UUID ownerId, AccountStatus status, Long amountCents, String currency, Instant createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;
        this.amountCents = amountCents;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Long getAmountCents() {
        return amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void credit(BigDecimal amount) {
        validateAmount(amount);
        this.amountCents += MoneyConverter.decimalToCents(amount);
    }

    public void debit(BigDecimal amount) {
        validateAmount(amount);
        Long amountToDeductCents = MoneyConverter.decimalToCents(amount);

        if (this.amountCents < amountToDeductCents) {
            throw new InsufficientBalanceException(getBalance(), amount);
        }

        this.amountCents -= amountToDeductCents;
    }


    public BigDecimal getBalance() {
        return MoneyConverter.centsToDecimal(this.amountCents);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Amount must be positive");
        }
    }
}
