package com.marcosprado.transactionautorizationservice.domain.strategy;

import com.marcosprado.transactionautorizationservice.domain.exception.CurrencyMismatchException;
import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Currency;
import com.marcosprado.transactionautorizationservice.domain.model.Transaction;
import com.marcosprado.transactionautorizationservice.domain.repository.AccountRepository;
import com.marcosprado.transactionautorizationservice.domain.repository.TransactionRepository;
import com.marcosprado.transactionautorizationservice.domain.util.MoneyConverter;
import com.marcosprado.transactionautorizationservice.presentation.dto.*;
import jakarta.transaction.Transactional;

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

    protected TransactionResponse generateTransactionResponse(Transaction transaction, Account account) {
        return new TransactionResponse(
                new TransactionDTO(
                        transaction.getId(),
                        transaction.getType(),
                        new MoneyDTO(
                                MoneyConverter.centsToDecimal(transaction.getAmountCents()),
                                transaction.getCurrencyCode()
                        ),
                        transaction.getStatus(),
                        transaction.getCreatedAt()
                ),
                new AccountBalanceDTO(
                        account.getId(),
                        new AccountBalanceDTO.BalanceDTO(
                                account.getBalance(),
                                account.getCurrencyCode()
                        )
                )
        );
    }

    protected void validateCurrency(String requestCurrency, Currency accountCurrency) {
        Currency requestCurrencyEnum = Currency.fromCode(requestCurrency);

        if (requestCurrencyEnum != accountCurrency) {
            throw new CurrencyMismatchException(
                    requestCurrency,
                    accountCurrency.name()
            );
        }
    }
}
