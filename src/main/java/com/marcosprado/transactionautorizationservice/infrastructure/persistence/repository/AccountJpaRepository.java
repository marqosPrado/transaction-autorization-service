package com.marcosprado.transactionautorizationservice.infrastructure.persistence.repository;

import com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {
}
