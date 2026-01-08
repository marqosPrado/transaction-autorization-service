package com.marcosprado.transactionautorizationservice.domain.repository;

import com.marcosprado.transactionautorizationservice.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(UUID id);
    void save(Account account);
    boolean existsById(UUID id);
}
