package com.marcosprado.transactionautorizationservice.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyConverter {

    private static final BigDecimal CENTS_DIVISOR = new BigDecimal("100");

    public static BigDecimal centsToDecimal(Long amountCents) {
        if (amountCents == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(amountCents)
                .divide(CENTS_DIVISOR, 2, RoundingMode.HALF_UP);
    }

    public static Long decimalToCents(BigDecimal amount) {
        if (amount == null) {
            return 0L;
        }
        return amount.multiply(CENTS_DIVISOR)
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();
    }
}
