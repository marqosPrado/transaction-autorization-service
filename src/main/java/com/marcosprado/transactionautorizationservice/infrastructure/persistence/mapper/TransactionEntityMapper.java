package com.marcosprado.transactionautorizationservice.infrastructure.persistence.mapper;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Transaction;
import com.marcosprado.transactionautorizationservice.domain.model.TransactionStatus;
import com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity.AccountEntity;
import com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionEntityMapper {

    private final AccountEntityMapper accountEntityMapper;

    public TransactionEntityMapper(AccountEntityMapper accountEntityMapper) {
        this.accountEntityMapper = accountEntityMapper;
    }

    public TransactionEntity toEntity(Transaction transaction, Account account) {
        AccountEntity accountEntity = accountEntityMapper.toEntity(account);
        return new TransactionEntity(
                accountEntity,
                transaction.getType(),
                transaction.getAmountCents(),
                transaction.getCurrency(),
                transaction.getCreatedAt(),
                transaction.getStatus().name()
        );
    }

    public Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getAccount().getId(),
                entity.getType(),
                entity.getAmountCents(),
                entity.getCurrency(),
                entity.getCreatedAt(),
                TransactionStatus.valueOf(entity.getStatus())
        );
    }
}
