package com.marcosprado.transactionautorizationservice.presentation.validation;

import com.marcosprado.transactionautorizationservice.domain.model.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return Currency.isSupported(value);
    }
}
