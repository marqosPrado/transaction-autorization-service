package com.marcosprado.transactionautorizationservice.domain.model;

import com.marcosprado.transactionautorizationservice.domain.exception.InsufficientBalanceException;
import com.marcosprado.transactionautorizationservice.domain.exception.InvalidOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Account Debit Business Logic Tests")
class AccountDebitTest {

    private Account account;
    private UUID accountId;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should successfully debit amount from account with sufficient balance")
    void shouldSuccessfullyDebitAmountFromAccountWithSufficientBalance() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("50.00");

        account.debit(debitAmount);

        assertThat(account.getAmountCents()).isEqualTo(5000L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Should successfully debit exact balance and zero the account")
    void shouldSuccessfullyDebitExactBalanceAndZeroTheAccount() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("100.00");

        account.debit(debitAmount);

        assertThat(account.getAmountCents()).isEqualTo(0L);
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should correctly handle decimal amounts in debit operation")
    void shouldCorrectlyHandleDecimalAmountsInDebitOperation() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                50000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("123.45");

        account.debit(debitAmount);

        assertThat(account.getAmountCents()).isEqualTo(37655L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("376.55"));
    }

    @Test
    @DisplayName("Should correctly handle small decimal amounts")
    void shouldCorrectlyHandleSmallDecimalAmounts() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                1000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("0.01");

        account.debit(debitAmount);

        assertThat(account.getAmountCents()).isEqualTo(999L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("9.99"));
    }

    @Test
    @DisplayName("Should handle multiple consecutive debit operations")
    void shouldHandleMultipleConsecutiveDebitOperations() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );

        account.debit(new BigDecimal("25.00"));
        account.debit(new BigDecimal("30.00"));
        account.debit(new BigDecimal("20.00"));

        assertThat(account.getAmountCents()).isEqualTo(2500L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("25.00"));
    }

    @Test
    @DisplayName("Should throw exception when debit amount exceeds balance")
    void shouldThrowExceptionWhenDebitAmountExceedsBalance() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("150.00");

        assertThatThrownBy(() -> account.debit(debitAmount))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance");

        assertThat(account.getAmountCents()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("Should throw exception when debit amount exceeds balance by small amount")
    void shouldThrowExceptionWhenDebitAmountExceedsBalanceBySmallAmount() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("100.01");

        assertThatThrownBy(() -> account.debit(debitAmount))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance");

        assertThat(account.getAmountCents()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("Should throw exception when trying to debit from zero balance account")
    void shouldThrowExceptionWhenTryingToDebitFromZeroBalanceAccount() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                0L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("50.00");

        assertThatThrownBy(() -> account.debit(debitAmount))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance");

        assertThat(account.getAmountCents()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should throw exception when debit amount is null")
    void shouldThrowExceptionWhenDebitAmountIsNull() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );

        assertThatThrownBy(() -> account.debit(null))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Amount must be positive");

        assertThat(account.getAmountCents()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("Should throw exception when debit amount is zero")
    void shouldThrowExceptionWhenDebitAmountIsZero() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = BigDecimal.ZERO;

        assertThatThrownBy(() -> account.debit(debitAmount))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Amount must be positive");

        assertThat(account.getAmountCents()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("Should throw exception when debit amount is negative")
    void shouldThrowExceptionWhenDebitAmountIsNegative() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("-50.00");

        assertThatThrownBy(() -> account.debit(debitAmount))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Amount must be positive");

        assertThat(account.getAmountCents()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("Should handle debit with amount having more than 2 decimal places")
    void shouldHandleDebitWithAmountHavingMoreThan2DecimalPlaces() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("50.555");

        account.debit(debitAmount);

        assertThat(account.getAmountCents()).isEqualTo(4944L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("49.44"));
    }

    @Test
    @DisplayName("Should preserve account properties after debit operation")
    void shouldPreserveAccountPropertiesAfterDebitOperation() {
        Instant createdAt = Instant.now();
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                createdAt
        );
        BigDecimal debitAmount = new BigDecimal("50.00");

        account.debit(debitAmount);

        assertThat(account.getId()).isEqualTo(accountId);
        assertThat(account.getOwnerId()).isEqualTo(ownerId);
        assertThat(account.getStatus()).isEqualTo(AccountStatus.ENABLED);
        assertThat(account.getCurrency()).isEqualTo(Currency.BRL);
        assertThat(account.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should handle alternating credit and debit operations")
    void shouldHandleAlternatingCreditAndDebitOperations() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );

        account.credit(new BigDecimal("50.00"));
        account.debit(new BigDecimal("30.00"));
        account.credit(new BigDecimal("20.00"));
        account.debit(new BigDecimal("40.00"));

        assertThat(account.getAmountCents()).isEqualTo(10000L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Should correctly handle large debit amounts")
    void shouldCorrectlyHandleLargeDebitAmounts() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                100000000L,
                Currency.BRL,
                Instant.now()
        );
        BigDecimal debitAmount = new BigDecimal("999999.99");

        account.debit(debitAmount);

        assertThat(account.getAmountCents()).isEqualTo(1L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("0.01"));
    }

    @Test
    @DisplayName("Should accumulate debits correctly with floating point precision")
    void shouldAccumulateDebitsCorrectlyWithFloatingPointPrecision() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                100L,
                Currency.BRL,
                Instant.now()
        );

        for (int i = 0; i < 10; i++) {
            account.debit(new BigDecimal("0.01"));
        }

        assertThat(account.getAmountCents()).isEqualTo(90L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("0.90"));
    }

    @Test
    @DisplayName("Should throw exception after multiple debits deplete balance")
    void shouldThrowExceptionAfterMultipleDebitsDepletBalance() {
        account = new Account(
                accountId,
                ownerId,
                AccountStatus.ENABLED,
                10000L,
                Currency.BRL,
                Instant.now()
        );

        account.debit(new BigDecimal("40.00"));
        account.debit(new BigDecimal("30.00"));
        account.debit(new BigDecimal("20.00"));

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("10.00"));

        assertThatThrownBy(() -> account.debit(new BigDecimal("20.00")))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance");

        assertThat(account.getAmountCents()).isEqualTo(1000L);
    }
}
