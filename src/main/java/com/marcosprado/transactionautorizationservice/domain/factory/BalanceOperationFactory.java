package com.marcosprado.transactionautorizationservice.domain.factory;

import com.marcosprado.transactionautorizationservice.domain.strategy.BalanceOperation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BalanceOperationFactory {

    private final Map<String, BalanceOperation> strategies;

    public BalanceOperationFactory(List<BalanceOperation> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        BalanceOperation::getOperationType,
                        Function.identity()
                ));
    }

    public BalanceOperation getStrategy(String operationType) {
        BalanceOperation strategy = strategies.get(operationType.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid operation type: " + operationType);
        }

        return strategy;
    }
}
