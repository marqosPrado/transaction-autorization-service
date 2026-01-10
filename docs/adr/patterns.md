# Padrões Utilizados

## Contexto

O sistema precisa processar diferentes tipos de operações financeiras (crédito, débito) em contas bancárias. No futuro, pode ser necessário adicionar novos tipos de operações como:

- **Estorno** (reversal): Desfaz uma transação anterior
- **Bloqueio** (hold): Reserva valor sem debitar
- **Transferência**: Débito + crédito entre contas

### O Problema

Como implementar múltiplos tipos de operações de forma que:
1. **Seja fácil adicionar novos tipos** sem modificar código existente
2. **Cada operação tenha sua lógica específica** de validação e execução
3. **O código permanece testável e manutenível**
4. **Evite-se grandes if/else ou switch**

### Decisão

**Escolhi o Strategy Pattern com Factory pela possibilidade de gerenciar outros tipos de trasações.**

### Implementação

#### Classe Abstrata

```java
public abstract class BalanceOperation {
   protected AccountRepository accountRepository;
   protected TransactionRepository transactionRepository;

   public BalanceOperation(AccountRepository accountRepository, TransactionRepository transactionRepository) {
      this.accountRepository = accountRepository;
      this.transactionRepository = transactionRepository;
   }

   @Transactional
   public abstract TransactionResponse execute(Account account, CreateTransactionRequest input);

   public abstract String getOperationType();

   protected TransactionResponse generateTransactionResponse(Transaction transaction, Account account) {
      return new TransactionResponse(
              new TransactionDTO(
                      transaction.getId(),
                      transaction.getType(),
                      new MoneyDTO(
                              MoneyConverter.centsToDecimal(transaction.getAmountCents()),
                              transaction.getCurrency()
                      ),
                      transaction.getStatus(),
                      transaction.getCreatedAt()
              ),
              new AccountBalanceDTO(
                      account.getId(),
                      new AccountBalanceDTO.BalanceDTO(
                              account.getBalance(),
                              account.getCurrency()
                      )
              )
      );
   }
}
```

#### Classes Concretas

**ProcessCreditOperation:**
```java
@Component
public class ProcessCreditOperation extends BalanceOperation {

   public ProcessCreditOperation(AccountRepository accountRepository, TransactionRepository transactionRepository) {
      super(accountRepository, transactionRepository);
   }

   @Override
   @Transactional
   public TransactionResponse execute(Account account, CreateTransactionRequest input) {
      BigDecimal amount = new BigDecimal(input.value());

      account.credit(amount);

      Transaction transaction = Transaction.create(
              account.getId(),
              TransactionOperationType.CREDIT,
              amount,
              account.getCurrency()
      );

      transaction.updateStatus(TransactionStatus.SUCCEEDED);

      accountRepository.save(account);
      Transaction result = transactionRepository.save(transaction, account);

      return generateTransactionResponse(result, account);
   }

   @Override
   public String getOperationType() {
      return TransactionOperationType.CREDIT.name();
   }
}
```

**ProcessDebitOperation:**
```java
@Component
public class ProcessDebitOperation extends BalanceOperation {

   public ProcessDebitOperation(AccountRepository accountRepository, TransactionRepository transactionRepository) {
      super(accountRepository, transactionRepository);
   }

   @Override
   @Transactional
   public TransactionResponse execute(Account account, CreateTransactionRequest input) {
      BigDecimal amount = new BigDecimal(input.value());

      account.debit(amount);

      Transaction transaction = Transaction.create(
              account.getId(),
              TransactionOperationType.DEBIT,
              amount,
              account.getCurrency()
      );

      transaction.updateStatus(TransactionStatus.SUCCEEDED);

      accountRepository.save(account);
      Transaction result = transactionRepository.save(transaction, account);

      return generateTransactionResponse(result, account);
   }

   @Override
   public String getOperationType() {
      return TransactionOperationType.DEBIT.name();
   }
}
```

#### Factory

```java
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
```