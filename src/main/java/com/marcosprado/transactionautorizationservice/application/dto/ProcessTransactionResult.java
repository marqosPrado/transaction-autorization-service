package com.marcosprado.transactionautorizationservice.application.dto;

import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.model.Transaction;

public record ProcessTransactionResult(
        Transaction transaction,
        Account account
) {
}
