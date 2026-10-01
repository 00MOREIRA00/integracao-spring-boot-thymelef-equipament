# 009 — EquipmentController

- **Task:** Criar `EquipmentController`
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Criado `EquipmentController.java` no pacote `controller` com cinco endpoints seguindo o `contract.md`:

| Método | Rota                   | Status de sucesso |
|--------|------------------------|-------------------|
| GET    | `/api/equipments`      | 200               |
| GET    | `/api/equipments/{id}` | 200               |
| POST   | `/api/equipments`      | 201               |
| PUT    | `/api/equipments/{id}` | 200               |
| DELETE | `/api/equipments/{id}` | 204               |

---

## Por que foi feito assim

**`@RestController`:** combina `@Controller` com `@ResponseBody` — todos os métodos serializam o retorno como JSON automaticamente. Sem isso, o Spring MVC tentaria resolver uma view pelo nome retornado.

**`@RequestMapping("/api/equipments")`:** define o prefixo de rota para todos os endpoints da classe. Evita repetir o caminho em cada método.

**`@Valid` no request body:** ativa as validações declaradas no `EquipmentRequest` (`@NotBlank`, `@NotNull`). Sem ele, as anotações de validação são ignoradas. Quando a validação falha, o Spring lança `MethodArgumentNotValidException`, que o `GlobalExceptionHandler` converte em `400 Bad Request`.

**`ResponseEntity`:** permite controlar explicitamente o status HTTP da resposta. O `create` retorna `201 Created`, o `delete` retorna `204 No Content` (sem body) — detalhes que o `@ResponseBody` sozinho não controlaria.

**Controller sem lógica:** o controller só recebe o request, chama o service e devolve o response. Nenhuma regra de negócio, nenhum acesso ao repository diretamente — exatamente como definido no `ARCHITECTURE.md`.

---

## O que aprendi

A diferença entre `200 OK` e `201 Created` é semântica: `200` significa "a operação teve sucesso", `201` significa "um recurso foi criado". Clientes e ferramentas como Postman e OpenAPI usam esses códigos para entender o que aconteceu — usar o código correto é parte do contrato da API.

`204 No Content` no DELETE indica sucesso sem corpo de resposta. Tentar serializar `void` com `@ResponseBody` causaria problemas — por isso o retorno é `ResponseEntity<Void>` e o body é construído com `.noContent().build()`.

---

## Dificuldades e como resolvi

Nenhuma dificuldade nesta task.
