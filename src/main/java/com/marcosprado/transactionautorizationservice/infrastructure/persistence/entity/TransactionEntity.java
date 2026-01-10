package com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity;

import com.marcosprado.transactionautorizationservice.domain.model.TransactionOperationType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private TransactionOperationType type;

    @Column(name = "amount_cents", nullable = false)
    private Long amountCents;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "status", nullable = false, length = 10)
    private String status;

    public TransactionEntity(
            AccountEntity account,
            TransactionOperationType type,
            Long amountCents,
            String currency,
            Instant createdAt,
            String status
    ) {
        this.account = account;
        this.type = type;
        this.amountCents = amountCents;
        this.currency = currency;
        this.createdAt = createdAt;
        this.status = status;
    }

    public TransactionEntity() {}

    public UUID getId() {
        return id;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public TransactionOperationType getType() {
        return type;
    }

    public void setType(TransactionOperationType type) {
        this.type = type;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
