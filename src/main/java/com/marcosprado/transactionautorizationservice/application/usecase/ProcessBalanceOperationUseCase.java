package com.marcosprado.transactionautorizationservice.application.usecase;

import com.marcosprado.transactionautorizationservice.domain.factory.BalanceOperationFactory;
import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.repository.AccountRepository;
import com.marcosprado.transactionautorizationservice.domain.strategy.BalanceOperation;
import com.marcosprado.transactionautorizationservice.presentation.dto.CreateTransactionRequest;
import com.marcosprado.transactionautorizationservice.presentation.dto.TransactionResponse;
import org.springframework.stereotype.Service;

@Service
public class ProcessBalanceOperationUseCase {

    private final AccountRepository accountRepository;
    private final BalanceOperationFactory balanceOperationFactory;

    public ProcessBalanceOperationUseCase(AccountRepository accountRepository, BalanceOperationFactory balanceOperationFactory) {
        this.accountRepository = accountRepository;
        this.balanceOperationFactory = balanceOperationFactory;
    }

    public TransactionResponse execute(CreateTransactionRequest input) {
        Account account = accountRepository.findById(input.accountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        BalanceOperation operation = balanceOperationFactory.getStrategy(input.operationType().name());

        return operation.execute(account, input);
    }
}
