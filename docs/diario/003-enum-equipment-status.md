# 003 — Enum EquipmentStatus

- **Task:** Criar enum `EquipmentStatus`
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Criado `EquipmentStatus.java` no pacote `model` com três valores: `DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO`.

---

## Por que foi feito assim

**Por que enum e não String?** Com String, qualquer valor poderia ser gravado no banco — "disponivel", "Disponível", "livre" — sem que o compilador reclamasse. O enum fecha o conjunto de valores válidos em tempo de compilação: se alguém tentar usar um valor que não existe, o erro aparece antes de rodar a aplicação.

**Por que no pacote `model`?** O enum faz parte do domínio da aplicação — representa um estado de negócio do equipamento. O pacote `model` agrupa entidades JPA e tipos do domínio, que é onde esse conceito mora.

**Como o JPA persiste o enum?** Por padrão o JPA grava o índice numérico do valor (0, 1, 2...), o que é frágil — reordenar os valores quebraria os dados existentes. A solução é usar `@Enumerated(EnumType.STRING)` na entidade, que grava o nome (`DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO`) em vez do índice. Isso será feito na entidade `Equipment`.

---

## O que aprendi

Enums em Java são tipos de primeira classe — podem ter métodos, campos e implementar interfaces. Para este caso o enum simples basta, mas é útil saber que enums podem carregar comportamento quando necessário.

A anotação `@Enumerated(EnumType.STRING)` é uma decisão importante: sem ela o JPA usa `ORDINAL` por padrão, o que significa que a ordem de declaração dos valores no enum vira um contrato implícito com o banco — perigoso em sistemas que evoluem.

---

## Dificuldades e como resolvi

Nenhuma dificuldade nesta task.
