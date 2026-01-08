package com.marcosprado.transactionautorizationservice.presentation.controller;

import com.marcosprado.transactionautorizationservice.application.usecase.ProcessBalanceOperationUseCase;
import com.marcosprado.transactionautorizationservice.presentation.dto.CreateTransactionRequest;
import com.marcosprado.transactionautorizationservice.presentation.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final ProcessBalanceOperationUseCase processBalanceOperationUseCase;

    public TransactionController(ProcessBalanceOperationUseCase processBalanceOperationUseCase) {
        this.processBalanceOperationUseCase = processBalanceOperationUseCase;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        log.info("Creating transaction for account: {}, type: {}, amount: {}",
                request.accountId(), request.operationType(), request.value());

        TransactionResponse result = processBalanceOperationUseCase.execute(request);

        return null;
    }

    private BigDecimal centsToDecimal(Long cents) {
        return BigDecimal.valueOf(cents)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
