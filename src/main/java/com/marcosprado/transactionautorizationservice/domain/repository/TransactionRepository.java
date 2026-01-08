package com.marcosprado.transactionautorizationservice.domain.repository;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Transaction;

public interface TransactionRepository {
    public Transaction save(Transaction transaction, Account account);
}
