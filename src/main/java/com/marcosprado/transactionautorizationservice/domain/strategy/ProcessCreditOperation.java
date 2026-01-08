package com.marcosprado.transactionautorizationservice.domain.strategy;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Transaction;
import com.marcosprado.transactionautorizationservice.domain.model.TransactionOperationType;
import com.marcosprado.transactionautorizationservice.domain.repository.AccountRepository;
import com.marcosprado.transactionautorizationservice.domain.repository.TransactionRepository;
import com.marcosprado.transactionautorizationservice.presentation.dto.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProcessCreditOperation extends BalanceOperation {

    public ProcessCreditOperation(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        super(accountRepository, transactionRepository);
    }

    @Override
    public TransactionResponse execute(Account account, CreateTransactionRequest input) {
        BigDecimal actualAmount = new BigDecimal(account.getAmountCents());

        Long value = input.value();
        if (!this.isValidValue(value)) {
            throw new IllegalArgumentException("Invalid value");
        }

        BigDecimal addValue = new BigDecimal(value);

        Long newBalance = actualAmount.add(addValue).longValue();
        account.setAmountCents(newBalance);

        Transaction transaction = Transaction.create(
                account.getId(),
                TransactionOperationType.CREDIT,
                newBalance,
                account.getCurrency()
        );

        accountRepository.save(account);
        Transaction result = transactionRepository.save(transaction, account);

        return this.generateTransactionResponse(result, account);
    }

    @Override
    public String getOperationType() {
        return TransactionOperationType.CREDIT.name();
    }
}
