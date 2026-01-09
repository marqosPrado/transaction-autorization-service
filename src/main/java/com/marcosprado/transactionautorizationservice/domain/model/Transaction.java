package com.marcosprado.transactionautorizationservice.domain.model;

import com.marcosprado.transactionautorizationservice.domain.util.MoneyConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private UUID accountId;
    private TransactionOperationType type;
    private Long amountCents;
    private String currency;
    private LocalDateTime createdAt;
    private TransactionStatus status;

    public Transaction(
            UUID id,
            UUID accountId,
            TransactionOperationType type,
            Long amountCents,
            String currency,
            LocalDateTime createdAt,
            TransactionStatus status
    ) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amountCents = amountCents;
        this.currency = currency;
        this.createdAt = createdAt;
        this.status = status;
    }

    private Transaction(UUID accountId, TransactionOperationType type, Long amountCents, String currency) {
        this.accountId = accountId;
        this.type = type;
        this.amountCents = amountCents;
        this.currency = currency;
        this.createdAt = LocalDateTime.now();
        this.status = TransactionStatus.PROCESS;
    }

    public static Transaction create(UUID accountId, TransactionOperationType type, BigDecimal amount, String currency) {
        Long amountCents = MoneyConverter.decimalToCents(amount);
        return new Transaction(accountId, type, amountCents, currency);
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public TransactionOperationType getType() {
        return type;
    }

    public Long getAmountCents() {
        return amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void updateStatus(TransactionStatus status) {
        this.status = status;
    }
}
