package com.marcosprado.transactionautorizationservice.infrastructure.persistence.repository.account;

import com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByOwnerId(UUID ownerId);
}
