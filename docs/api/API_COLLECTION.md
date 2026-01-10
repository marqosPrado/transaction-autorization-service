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
    "amount": 150.00,
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
    "type": "DEBIT",
    "amount": 50.00,
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
    "type": "DEBIT",
    "amount": 200.00,
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (201 Created):**

```json
{
  "transaction": {
    "id": "af9bf00a-d376-6ad7-bg5g-775957ee6765",
    "type": "DEBIT",
    "amount": {
      "value": 200.00,
      "currency": "BRL"
    },
    "status": "FAILED",
    "timestamp": "2026-01-09T15:32:00-03:00"
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

**Nota:** O status HTTP é 201 (transação foi criada), mas `transaction.status` é `FAILED` indicando que a operação não foi executada devido a saldo insuficiente.

---

### 4. Conta Não Encontrada

Tenta criar transação em uma conta que não existe.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "00000000-0000-0000-0000-000000000000",
    "type": "CREDIT",
    "amount": 50.00,
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (404 Not Found):**

```json
{
  "timestamp": "2026-01-09T15:33:00-03:00",
  "status": 404,
  "error": "Not Found",
  "message": "Account not found with id: 00000000-0000-0000-0000-000000000000",
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
    "type": "CREDIT",
    "amount": -100.00,
    "currency": "BRL"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2026-01-09T15:34:00-03:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "amount",
      "message": "must be greater than 0"
    }
  ],
  "path": "/api/v1/transactions"
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
  "timestamp": "2026-01-09T15:35:00-03:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "type",
      "message": "must not be null"
    }
  ],
  "path": "/api/v1/transactions"
}
```

---

### 7. Validação - Moeda Inválida

Tenta criar transação com código de moeda inválido.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
    "type": "CREDIT",
    "amount": 100.00,
    "currency": "INVALID"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | jq .
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2026-01-09T15:36:00-03:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid currency code: INVALID",
  "path": "/api/v1/transactions"
}
```

---

### 8. Múltiplas Transações Sequenciais

Script para executar múltiplas transações em sequência.

```bash
#!/bin/bash

ACCOUNT_ID="5b19c8b6-0cc4-4c72-a989-0c2ee15fa975"
BASE_URL="http://localhost:8080/api/v1/transactions"

echo "=== Crédito inicial: R$ 1000.00 ==="
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d "{\"accountId\":\"$ACCOUNT_ID\",\"type\":\"CREDIT\",\"amount\":1000.00,\"currency\":\"BRL\"}" \
  -s | jq '.account.balance'

echo -e "\n=== Débito: R$ 250.00 ==="
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d "{\"accountId\":\"$ACCOUNT_ID\",\"type\":\"DEBIT\",\"amount\":250.00,\"currency\":\"BRL\"}" \
  -s | jq '.account.balance'

echo -e "\n=== Débito: R$ 300.00 ==="
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d "{\"accountId\":\"$ACCOUNT_ID\",\"type\":\"DEBIT\",\"amount\":300.00,\"currency\":\"BRL\"}" \
  -s | jq '.account.balance'

echo -e "\n=== Crédito: R$ 50.00 ==="
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d "{\"accountId\":\"$ACCOUNT_ID\",\"type\":\"CREDIT\",\"amount\":50.00,\"currency\":\"BRL\"}" \
  -s | jq '.account.balance'

echo -e "\n=== Saldo final esperado: R$ 500.00 ==="
```

**Output esperado:**

```json
{
  "amount": 1000.00,
  "currency": "BRL"
}

{
  "amount": 750.00,
  "currency": "BRL"
}

{
  "amount": 450.00,
  "currency": "BRL"
}

{
  "amount": 500.00,
  "currency": "BRL"
}

=== Saldo final esperado: R$ 500.00 ===
```

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
    "name": "Transaction Authorization Service API",
    "description": "Collection for testing transaction authorization endpoints",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080",
      "type": "string"
    },
    {
      "key": "accountId",
      "value": "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
      "type": "string"
    }
  ],
  "item": [
    {
      "name": "Health Check",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{baseUrl}}/actuator/health",
          "host": ["{{baseUrl}}"],
          "path": ["actuator", "health"]
        }
      },
      "response": []
    },
    {
      "name": "Create Credit Transaction - Success",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "pm.test(\"Status code is 201\", function () {",
              "    pm.response.to.have.status(201);",
              "});",
              "",
              "pm.test(\"Transaction status is SUCCEEDED\", function () {",
              "    var jsonData = pm.response.json();",
              "    pm.expect(jsonData.transaction.status).to.eql(\"SUCCEEDED\");",
              "});",
              "",
              "pm.test(\"Transaction type is CREDIT\", function () {",
              "    var jsonData = pm.response.json();",
              "    pm.expect(jsonData.transaction.type).to.eql(\"CREDIT\");",
              "});"
            ]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"accountId\": \"{{accountId}}\",\n  \"type\": \"CREDIT\",\n  \"amount\": 150.00,\n  \"currency\": \"BRL\"\n}"
        },
        "url": {
          "raw": "{{baseUrl}}/api/v1/transactions",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "transactions"]
        }
      },
      "response": []
    },
    {
      "name": "Create Debit Transaction - Success",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "pm.test(\"Status code is 201\", function () {",
              "    pm.response.to.have.status(201);",
              "});",
              "",
              "pm.test(\"Transaction status is SUCCEEDED\", function () {",
              "    var jsonData = pm.response.json();",
              "    pm.expect(jsonData.transaction.status).to.eql(\"SUCCEEDED\");",
              "});"
            ]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"accountId\": \"{{accountId}}\",\n  \"type\": \"DEBIT\",\n  \"amount\": 50.00,\n  \"currency\": \"BRL\"\n}"
        },
        "url": {
          "raw": "{{baseUrl}}/api/v1/transactions",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "transactions"]
        }
      },
      "response": []
    },
    {
      "name": "Create Debit Transaction - Insufficient Balance",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "pm.test(\"Status code is 201\", function () {",
              "    pm.response.to.have.status(201);",
              "});",
              "",
              "pm.test(\"Transaction status is FAILED\", function () {",
              "    var jsonData = pm.response.json();",
              "    pm.expect(jsonData.transaction.status).to.eql(\"FAILED\");",
              "});"
            ]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"accountId\": \"{{accountId}}\",\n  \"type\": \"DEBIT\",\n  \"amount\": 10000.00,\n  \"currency\": \"BRL\"\n}"
        },
        "url": {
          "raw": "{{baseUrl}}/api/v1/transactions",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "transactions"]
        }
      },
      "response": []
    },
    {
      "name": "Create Transaction - Account Not Found",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "pm.test(\"Status code is 404\", function () {",
              "    pm.response.to.have.status(404);",
              "});"
            ]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"accountId\": \"00000000-0000-0000-0000-000000000000\",\n  \"type\": \"CREDIT\",\n  \"amount\": 100.00,\n  \"currency\": \"BRL\"\n}"
        },
        "url": {
          "raw": "{{baseUrl}}/api/v1/transactions",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "transactions"]
        }
      },
      "response": []
    },
    {
      "name": "Create Transaction - Negative Amount",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "pm.test(\"Status code is 400\", function () {",
              "    pm.response.to.have.status(400);",
              "});"
            ]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"accountId\": \"{{accountId}}\",\n  \"type\": \"CREDIT\",\n  \"amount\": -100.00,\n  \"currency\": \"BRL\"\n}"
        },
        "url": {
          "raw": "{{baseUrl}}/api/v1/transactions",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "transactions"]
        }
      },
      "response": []
    },
    {
      "name": "Create Transaction - Invalid Currency",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "pm.test(\"Status code is 400\", function () {",
              "    pm.response.to.have.status(400);",
              "});"
            ]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"accountId\": \"{{accountId}}\",\n  \"type\": \"CREDIT\",\n  \"amount\": 100.00,\n  \"currency\": \"INVALID\"\n}"
        },
        "url": {
          "raw": "{{baseUrl}}/api/v1/transactions",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "transactions"]
        }
      },
      "response": []
    }
  ]
}
```

---
