package com.marcosprado.transactionautorizationservice.infrastructure.persistence.mapper;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Currency;
import com.marcosprado.transactionautorizationservice.infrastructure.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountEntityMapper {

    public AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity(
                account.getId(),
                account.getOwnerId(),
                account.getStatus(),
                account.getAmountCents(),
                account.getCurrency().name(),
                account.getCreatedAt()
        );
        entity.setVersion(account.getVersion());
        return entity;
    }

    public Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getOwnerId(),
                entity.getStatus(),
                entity.getAmountCents(),
                Currency.fromCode(entity.getCurrency()),
                entity.getCreatedAt(),
                entity.getVersion()
        );
    }
}
