package com.marcosprado.transactionautorizationservice.domain.model;

public enum Currency {
    BRL;

    public static Currency fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Currency code cannot be null");
        }
        try {
            return Currency.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported currency: " + code);
        }
    }

    public static boolean isSupported(String code) {
        if (code == null) {
            return false;
        }
        try {
            fromCode(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
