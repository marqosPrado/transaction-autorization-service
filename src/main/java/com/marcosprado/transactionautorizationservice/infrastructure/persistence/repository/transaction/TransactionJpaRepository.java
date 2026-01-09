package com.marcosprado.transactionautorizationservice.infrastructure.persistence.repository.transaction;

import com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
}
