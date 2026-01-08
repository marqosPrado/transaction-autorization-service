package com.marcosprado.transactionautorizationservice.domain.strategy;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Transaction;
import com.marcosprado.transactionautorizationservice.domain.repository.AccountRepository;
import com.marcosprado.transactionautorizationservice.domain.repository.TransactionRepository;
import com.marcosprado.transactionautorizationservice.presentation.dto.*;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

public abstract class BalanceOperation {
    protected AccountRepository accountRepository;
    protected TransactionRepository transactionRepository;

    public BalanceOperation(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public abstract TransactionResponse execute(Account account, CreateTransactionRequest input);

    public abstract String getOperationType();

    protected boolean isValidValue(Long value) {
        return value != null && value > 0;
    }

    protected TransactionResponse generateTransactionResponse(Transaction transaction, Account account) {
        return new TransactionResponse(
                new TransactionDTO(
                        transaction.getId(),
                        transaction.getType(),
                        new MoneyDTO(
                                new BigDecimal(transaction.getAmountCents()),
                                transaction.getCurrency()
                        ),
                        transaction.getStatus(),
                        transaction.getCreatedAt()
                ),
                new AccountBalanceDTO(
                        account.getId(),
                        new AccountBalanceDTO.BalanceDTO(
                                new BigDecimal(
                                        account.getAmountCents()
                                ),
                                account.getCurrency()
                        )
                )
        );
    }
}
