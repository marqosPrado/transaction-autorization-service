package com.marcosprado.transactionautorizationservice.domain.model;

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

    public void updateAmount(Long amountCents) {
        this.amountCents = amountCents;
    }

    public void updateStatus(AccountStatus status) {
        this.status = status;
    }
}
