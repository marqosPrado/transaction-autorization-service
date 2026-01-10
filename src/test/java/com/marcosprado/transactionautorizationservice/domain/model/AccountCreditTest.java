package com.marcosprado.transactionautorizationservice.domain.model;

import com.marcosprado.transactionautorizationservice.domain.exception.InvalidOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Account Credit Business Logic Tests")
class AccountCreditTest {

    private Account account;
    private UUID accountId;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should successfully credit amount to account with existing balance")
    void shouldSuccessfullyCreditAmountToAccountWithExistingBalance() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                "BRL",
                Instant.now()
        );
        BigDecimal creditAmount = new BigDecimal("50.00");

        account.credit(creditAmount);

        assertThat(account.getAmountCents()).isEqualTo(15000L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Should successfully credit amount to account with zero balance")
    void shouldSuccessfullyCreditAmountToAccountWithZeroBalance() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                0L,
                "BRL",
                Instant.now()
        );
        BigDecimal creditAmount = new BigDecimal("100.00");

        account.credit(creditAmount);

        assertThat(account.getAmountCents()).isEqualTo(10000L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Should correctly handle decimal amounts in credit operation")
    void shouldCorrectlyHandleDecimalAmountsInCreditOperation() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                50000L,
                "BRL",
                Instant.now()
        );
        BigDecimal creditAmount = new BigDecimal("123.45");

        account.credit(creditAmount);

        assertThat(account.getAmountCents()).isEqualTo(62345L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("623.45"));
    }

    @Test
    @DisplayName("Should correctly handle small decimal amounts")
    void shouldCorrectlyHandleSmallDecimalAmounts() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                1000L,
                "BRL",
                Instant.now()
        );
        BigDecimal creditAmount = new BigDecimal("0.01");

        account.credit(creditAmount);

        assertThat(account.getAmountCents()).isEqualTo(1001L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("10.01"));
    }

    @Test
    @DisplayName("Should handle multiple consecutive credit operations")
    void shouldHandleMultipleConsecutiveCreditOperations() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                "BRL",
                Instant.now()
        );

        account.credit(new BigDecimal("25.00"));
        account.credit(new BigDecimal("30.00"));
        account.credit(new BigDecimal("45.00"));

        assertThat(account.getAmountCents()).isEqualTo(20000L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    @DisplayName("Should throw exception when credit amount is null")
    void shouldThrowExceptionWhenCreditAmountIsNull() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                "BRL",
                Instant.now()
        );

        assertThatThrownBy(() -> account.credit(null))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Amount must be positive");

        assertThat(account.getAmountCents()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("Should throw exception when credit amount is zero")
    void shouldThrowExceptionWhenCreditAmountIsZero() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                "BRL",
                Instant.now()
        );
        BigDecimal creditAmount = BigDecimal.ZERO;

        assertThatThrownBy(() -> account.credit(creditAmount))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Amount must be positive");

        assertThat(account.getAmountCents()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("Should throw exception when credit amount is negative")
    void shouldThrowExceptionWhenCreditAmountIsNegative() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                "BRL",
                Instant.now()
        );
        BigDecimal creditAmount = new BigDecimal("-50.00");

        assertThatThrownBy(() -> account.credit(creditAmount))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Amount must be positive");

        assertThat(account.getAmountCents()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("Should handle large credit amounts")
    void shouldHandleLargeCreditAmounts() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                100000L,
                "BRL",
                Instant.now()
        );
        BigDecimal creditAmount = new BigDecimal("999999.99");

        account.credit(creditAmount);

        assertThat(account.getAmountCents()).isEqualTo(100099999L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("1000999.99"));
    }

    @Test
    @DisplayName("Should preserve account properties after credit operation")
    void shouldPreserveAccountPropertiesAfterCreditOperation() {
        Instant createdAt = Instant.now();
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                "BRL",
                createdAt
        );
        BigDecimal creditAmount = new BigDecimal("50.00");

        account.credit(creditAmount);

        assertThat(account.getId()).isEqualTo(accountId);
        assertThat(account.getOwnerId()).isEqualTo(ownerId);
        assertThat(account.getStatus()).isEqualTo(AccountStatus.ENABLED);
        assertThat(account.getCurrency()).isEqualTo("BRL");
        assertThat(account.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should accumulate credits correctly")
    void shouldAccumulateCreditsCorrectlyWithFloatingPointPrecision() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                0L,
                "BRL",
                Instant.now()
        );

        for (int i = 0; i < 10; i++) {
            account.credit(new BigDecimal("0.01"));
        }

        assertThat(account.getAmountCents()).isEqualTo(10L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("0.10"));
    }
}
