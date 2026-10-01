# 008 — GlobalExceptionHandler

- **Task:** Criar `GlobalExceptionHandler` com `@ControllerAdvice`
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Criado `GlobalExceptionHandler.java` no pacote `exception` com três handlers:
- `EquipmentNotFoundException` → `404 Not Found`
- `SerialNumberAlreadyExistsException` → `409 Conflict`
- `MethodArgumentNotValidException` → `400 Bad Request` (erros de validação do `@NotBlank` / `@NotNull`)

Todos retornam o mesmo envelope JSON: `{ "status": <código>, "message": "<mensagem>" }`.

---

## Por que foi feito assim

**`@RestControllerAdvice`:** combina `@ControllerAdvice` (intercepta exceções de todos os controllers) com `@ResponseBody` (serializa a resposta como JSON automaticamente). Centraliza o tratamento de erros em um único lugar — o controller não precisa de nenhum try/catch.

**Um `@ExceptionHandler` por tipo de exceção:** cada método captura um tipo específico e retorna o status HTTP correto. Isso é mais explícito e fácil de manter do que um handler genérico que tenta inferir o status a partir da exceção.

**`MethodArgumentNotValidException`:** lançada automaticamente pelo Spring quando uma requisição falha nas validações do `@Valid` (os `@NotBlank` e `@NotNull` do `EquipmentRequest`). O handler extrai a mensagem do primeiro campo inválido para devolver ao cliente.

**Envelope `{ "status", "message" }`:** formato definido no `contract.md`. Usar `Map.of(...)` é suficiente para este projeto — evita criar uma classe `ErrorResponse` desnecessária.

---

## O que aprendi

O `@ControllerAdvice` é um bean especial do Spring MVC que intercepta exceções lançadas por qualquer controller antes de a resposta ser enviada ao cliente. Sem ele, exceções não tratadas resultam em respostas genéricas do Spring (Whitelabel Error Page ou JSON com stacktrace) — que não são adequadas para uma API.

A separação entre exceção de negócio (`EquipmentNotFoundException`) e mapeamento HTTP (`GlobalExceptionHandler`) mantém o service agnóstico ao protocolo HTTP — o service só sabe que o equipamento não foi encontrado, não sabe que isso vira um `404`.

---

## Dificuldades e como resolvi

Nenhuma dificuldade nesta task.
