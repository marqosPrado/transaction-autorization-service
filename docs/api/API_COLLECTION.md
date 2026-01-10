# API Collection - Transaction Authorization Service

## Visão Geral

Este documento contém exemplos completos de uso da API do Transaction Authorization Service, incluindo:

- Exemplos de requisições com `curl`
- Coleção Postman (JSON)
- Casos de uso comuns
- Cenários de erro e tratamento

**Base URL:**
- **Local**: `http://localhost:8080`

## Endpoints Disponíveis

### POST /api/v1/transactions

Cria uma nova transação de crédito ou débito em uma conta bancária.

---

## Exemplos com cURL

### 1. Operação de Crédito - Sucesso

Adiciona R$ 150,00 a uma conta.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "type": "CREDIT",
    "value": "150.00",
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (201 Created):**

```json
{
  "transaction": {
    "id": "8e8ae808-b154-48b5-9f3e-553935cc4543",
    "type": "CREDIT",
    "amount": {
      "value": 150.00,
      "currency": "BRL"
    },
    "status": "SUCCEEDED",
    "timestamp": "2026-01-09T15:30:00-03:00"
  },
  "account": {
    "id": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "balance": {
      "amount": 150.00,
      "currency": "BRL"
    }
  }
}
```

---

### 2. Operação de Débito - Sucesso

Debita R\$ 50,00 de uma conta (assumindo saldo de R$ 150,00).

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "operationType": "DEBIT",
    "value": "50.00",
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (201 Created):**

```json
{
  "transaction": {
    "id": "9f8ae909-c265-59c6-af4f-664846dd5654",
    "type": "DEBIT",
    "amount": {
      "value": 50.00,
      "currency": "BRL"
    },
    "status": "SUCCEEDED",
    "timestamp": "2026-01-09T15:31:00-03:00"
  },
  "account": {
    "id": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "balance": {
      "amount": 100.00,
      "currency": "BRL"
    }
  }
}
```

---

### 3. Operação de Débito - Saldo Insuficiente

Tenta debitar R\$ 200,00 de uma conta com saldo de R\$ 100,00.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "operationType": "DEBIT",
    "value": "8000.00",
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (422):**

```json
{
  "timestamp": "2026-01-10T18:23:01.713082460Z",
  "status": 422,
  "message": "Insufficient balance. Current: 500.00, Requested: 8000.00",
  "errorCode": "INSUFFICIENT_BALANCE",
  "path": "/api/v1/transactions",
  "details": {
    "currentBalance": 500.00,
    "requestedAmount": 8000.00
  }
}
```

---

### 4. Conta Não Encontrada

Tenta criar transação em uma conta que não existe.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "9f2c4c7e-6b3a-4d6b-9a3e-8f1d7c2b5a41",
    "operationType": "CREDIT",
    "value": "50.00",
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (404 Not Found):**

```json
{
  "timestamp": "2026-01-10T18:24:28.348905467Z",
  "status": 404,
  "message": "Account with ID '9f2c4c7e-6b3a-4d6b-9a3e-8f1d7c2b5a41' not found",
  "errorCode": "ACCOUNT_NOT_FOUND",
  "path": "/api/v1/transactions"
}
```

---

### 5. Validação - Valor Negativo

Tenta criar transação com valor negativo (inválido).

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "operationType": "CREDIT",
    "value": -8000.00,
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2026-01-10T18:25:01.029199037Z",
  "status": 400,
  "message": "Validation failed for one or more fields",
  "errorCode": "VALIDATION_ERROR",
  "path": "/api/v1/transactions",
  "fieldErrors": [
    {
      "field": "value",
      "rejectedValue": "-8000.00",
      "message": "Amount must be positive"
    }
  ]
}
```

---

### 6. Validação - Campo Obrigatório Ausente

Tenta criar transação sem informar o tipo.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "amount": 100.00,
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2026-01-10T18:26:07.299998237Z",
  "status": 400,
  "message": "Validation failed for one or more fields",
  "errorCode": "VALIDATION_ERROR",
  "path": "/api/v1/transactions",
  "fieldErrors": [
    {
      "field": "operationType",
      "rejectedValue": null,
      "message": "Operation type is required"
    }
  ]
}
```

---

### 7. Validação - Moeda Não Suportada

Tenta criar transação com código de moeda não suportado.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "operationType": "CREDIT",
    "value": "100.00",
    "currency": "USD"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2026-01-10T18:01:50.581211527Z",
  "status": 400,
  "message": "Validation failed for one or more fields",
  "errorCode": "VALIDATION_ERROR",
  "path": "/api/v1/transactions",
  "fieldErrors": [
    {
      "field": "currency",
      "rejectedValue": "USD",
      "message": "Currency not supported"
    }
  ]
}
```

**Moedas suportadas:**
- `BRL` - Real

---

### 8. Validação - Incompatibilidade de Moeda

Tenta criar transação com moeda diferente da moeda da conta.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "operationType": "DEBIT",
    "value": "50.00",
    "currency": "EUR"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (422 Unprocessable Entity):**

```json
{
  "timestamp": "2026-01-10T18:05:23.123456789Z",
  "status": 422,
  "message": "Currency error. Request: EUR, Account: BRL",
  "errorCode": "CURRENCY_MISMATCH",
  "path": "/api/v1/transactions",
  "details": {
    "requestCurrency": "EUR",
    "accountCurrency": "BRL"
  }
}
```

**Nota:** Este erro ocorre quando a moeda informada na requisição é válida mas não corresponde à moeda configurada para a conta.

---

## Postman Collection

### Importar para o Postman

1. Abra o Postman
2. Click em "Import"
3. Selecione o arquivo `postman_collection.json` (abaixo)
4. Configure a variável `{{baseUrl}}` para o ambiente desejado

### Arquivo: `postman_collection.json`

```json
{
  "info": {
    "_postman_id": "4bad4129-aae1-4f41-9192-f20eb6ab8123",
    "name": "TRANSACTION SERVICE",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
    "_exporter_id": "40498346"
  },
  "item": [
    {
      "name": "DÉBITO",
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "raw",
          "raw": "{\r\n    \"accountId\": \"42aeb186-5995-4c57-a290-8a957e5fa4bb\",\r\n    \"operationType\": \"DEBIT\",\r\n    \"value\": \"19.00\",\r\n    \"currency\": \"BRL\"\r\n}",
          "options": {
            "raw": {
              "language": "json"
            }
          }
        },
        "url": {
          "raw": "http://localhost:8080/api/v1/transactions",
          "protocol": "http",
          "host": [
            "localhost"
          ],
          "port": "8080",
          "path": [
            "api",
            "v1",
            "transactions"
          ]
        }
      },
      "response": []
    },
    {
      "name": "CRÉDITO",
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "raw",
          "raw": "{\r\n    \"accountId\": \"42aeb186-5995-4c57-a290-8a957e5fa4bb\",\r\n    \"operationType\": \"CREDIT\",\r\n    \"value\": \"19.00\",\r\n    \"currency\": \"BRL\"\r\n}",
          "options": {
            "raw": {
              "language": "json"
            }
          }
        },
        "url": {
          "raw": "http://localhost:8080/api/v1/transactions",
          "protocol": "http",
          "host": [
            "localhost"
          ],
          "port": "8080",
          "path": [
            "api",
            "v1",
            "transactions"
          ]
        }
      },
      "response": []
    }
  ]
}
```
---
