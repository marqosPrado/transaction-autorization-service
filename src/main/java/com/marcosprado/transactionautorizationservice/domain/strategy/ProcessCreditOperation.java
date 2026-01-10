package com.marcosprado.transactionautorizationservice.domain.strategy;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Transaction;
import com.marcosprado.transactionautorizationservice.domain.model.TransactionOperationType;
import com.marcosprado.transactionautorizationservice.domain.model.TransactionStatus;
import com.marcosprado.transactionautorizationservice.domain.repository.AccountRepository;
import com.marcosprado.transactionautorizationservice.domain.repository.TransactionRepository;
import com.marcosprado.transactionautorizationservice.presentation.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProcessCreditOperation extends BalanceOperation {

    public ProcessCreditOperation(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        super(accountRepository, transactionRepository);
    }

    @Override
    @Transactional
    public TransactionResponse execute(Account account, CreateTransactionRequest input) {
        BigDecimal amount = new BigDecimal(input.value());

        validateCurrency(input.currency(), account.getCurrency());

        account.credit(amount);

        Transaction transaction = Transaction.create(
                account.getId(),
                TransactionOperationType.CREDIT,
                amount,
                account.getCurrency()
        );

        transaction.updateStatus(TransactionStatus.SUCCEEDED);

        accountRepository.save(account);
        Transaction result = transactionRepository.save(transaction, account);

        return generateTransactionResponse(result, account);
    }

    @Override
    public String getOperationType() {
        return TransactionOperationType.CREDIT.name();
    }
}
