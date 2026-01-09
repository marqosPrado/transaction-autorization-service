package com.marcosprado.transactionautorizationservice.infrastructure.persistence.repository.transaction;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Transaction;
import com.marcosprado.transactionautorizationservice.domain.repository.TransactionRepository;
import com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity.TransactionEntity;
import com.marcosprado.transactionautorizationservice.infrastructure.persistence.mapper.TransactionEntityMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionEntityMapper mapper;

    public TransactionRepositoryImpl(TransactionJpaRepository transactionJpaRepository, TransactionEntityMapper mapper) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Transaction save(Transaction transaction, Account account) {
        TransactionEntity entity = mapper.toEntity(transaction, account);
        TransactionEntity saved = transactionJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
