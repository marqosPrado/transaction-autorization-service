# Transaction Authorization Service - Documentação

## Índice Geral

Este diretório contém toda a documentação técnica do Transaction Authorization Service, organizada por categorias.

---

## Começando

- **[README Principal](../README.md)** - Visão geral do projeto, instalação e uso

## Decisões Arquiteturais

Os ADRs documentam decisões importantes tomadas durante o desenvolvimento do projeto, incluindo contexto, alternativas consideradas, justificativa e consequências.

### Strategy Pattern para Operações
**[Ver](adr/patterns.md)**

**Decisão:** Strategy Pattern para operações de crédito/débito

**Por quê:**
- Open/Closed Principle (fácil adicionar novos tipos)
- Testabilidade
- Extensibilidade para novos tipos de operação

**Trade-offs:**
- ✅ Código limpo e extensível
- ❌ Mais classes (uma por tipo de operação)

---

## API

### API Collection
**[Ver Documentação](api/API_COLLECTION.md)**

**Conteúdo:**
- Exemplos completos com `curl`
- Coleção Postman (JSON)
- Casos de uso (crédito, débito, saldo insuficiente)
- Cenários de erro (404, 400, validação)
- Health check endpoint
- Load testing com Apache Bench

**[Importar Postman Collection](../postman_collection.json)**

---

## Estrutura de Documentação

```
docs/
├── INDEX.md                          # Este arquivo
├── architecture/
│   └── ARCHITECTURE.md  
├── adr/
│   ├── ADR-001-database-choice.md    # Decisão: PostgreSQL
│   └── patterns.md
└── api/
    └── API_COLLECTION.md             # Exemplos de API
```

---

## Quick Links

### Para Desenvolvedores
- [Como rodar localmente](../README.md#instalação)
- [Arquitetura do código](architecture/ARCHITECTURE.md)
- [Padrões Utilizados](/docs/adr/patterns.md)
- [Exemplos de API](api/API_COLLECTION.md)

## Referências que utilizei

- [Strategy Pattern | Refactoring Guru](https://refactoring.guru/design-patterns/strategy)
- [Factory Pattern | Refactoring Guru](https://refactoring.guru/design-patterns/factory-method)
- [Implementing the Strategy Design pattern in Spring Boot](https://medium.com/codex/implementing-the-strategy-design-pattern-in-spring-boot-df3adb9ceb4a)
- [Floats Don’t Work For Storing Cents: Why Modern Treasury Uses Integers Instead](https://www.moderntreasury.com/journal/floats-dont-work-for-storing-cents?utm_source=chatgpt.com)