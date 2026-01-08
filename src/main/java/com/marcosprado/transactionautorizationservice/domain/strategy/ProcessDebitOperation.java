package com.marcosprado.transactionautorizationservice.domain.strategy;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.repository.AccountRepository;
import com.marcosprado.transactionautorizationservice.presentation.dto.CreateTransactionRequest;
import com.marcosprado.transactionautorizationservice.presentation.dto.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class ProcessDebitOperation extends BalanceOperation {

    public ProcessDebitOperation(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    public TransactionResponse execute(Account account, CreateTransactionRequest input) {
        return null;
    }

    @Override
    public String getOperationType() {
        return "DEBIT";
    }
}
