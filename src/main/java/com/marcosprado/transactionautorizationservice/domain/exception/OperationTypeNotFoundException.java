package com.marcosprado.transactionautorizationservice.domain.exception;

public class OperationTypeNotFoundException extends DomainException {

    private static final String ERROR_CODE = "OPERATION_TYPE_NOT_FOUND";

    public OperationTypeNotFoundException(String operationType) {
        super(String.format("Operation type '%s' is not supported", operationType), ERROR_CODE);
    }
}
