package com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity;

import com.marcosprado.transactionautorizationservice.domain.model.AccountStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "owner_id", nullable = false, updatable = false)
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 8)
    private AccountStatus status;

    @Column(name = "amount_cents", nullable = false)
    private Long amountCents = 0L;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency = "BRL";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public AccountEntity() {}

    public AccountEntity(UUID id, UUID ownerId, AccountStatus status, Long amountCents, String currency, Instant createdAt) {
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

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Long getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Long amountCents) {
        this.amountCents = amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
