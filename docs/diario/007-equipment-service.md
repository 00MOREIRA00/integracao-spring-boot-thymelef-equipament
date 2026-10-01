# 007 — EquipmentService e exceções de negócio

- **Task:** Criar `EquipmentService` + validação de número de série duplicado
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Criadas duas exceções de negócio no pacote `exception`:
- `EquipmentNotFoundException` — lançada quando um id não existe no banco
- `SerialNumberAlreadyExistsException` — lançada quando o número de série já está cadastrado

Criado `EquipmentService.java` no pacote `service` com os métodos: `findAll`, `findById`, `create`, `update`, `delete`.

---

## Por que foi feito assim

**`@RequiredArgsConstructor` + `final`:** o Lombok gera um construtor com todos os campos `final`, que o Spring usa para injeção de dependência via construtor. É a forma preferida em relação ao `@Autowired` no campo — facilita testes unitários (pode passar um mock diretamente no construtor) e torna as dependências explícitas.

**Exceções de negócio como `RuntimeException`:** exceções não checadas (`RuntimeException`) não obrigam o chamador a capturá-las com try/catch. Em Spring, o `@ControllerAdvice` intercepta essas exceções globalmente e converte para a resposta HTTP correta — o controller fica limpo.

**`DataIntegrityViolationException` → `SerialNumberAlreadyExistsException`:** quando o banco rejeita uma inserção por constraint de unicidade, o Spring lança `DataIntegrityViolationException`. Essa exceção é genérica demais para expor ao cliente. O service a captura e relança como uma exceção de negócio com mensagem legível — o `GlobalExceptionHandler` vai mapeá-la para `409 Conflict`.

**`repository.existsById(id)` no delete:** evita fazer `findById` só para verificar existência e descartar o objeto. `existsById` gera um `SELECT COUNT` ou `EXISTS` mais eficiente.

**`update` busca o equipamento antes de salvar:** o JPA precisa de uma entidade gerenciada (com `id` preenchido) para gerar um `UPDATE` em vez de `INSERT`. Buscar pelo id antes garante isso e também valida que o equipamento existe.

---

## O que aprendi

O Spring Data JPA distingue `save` de `insert` e `update` pelo estado da entidade: se o `id` for null, gera INSERT; se tiver id preenchido e a entidade for gerenciada, gera UPDATE. Por isso o `update` busca a entidade do banco antes de alterar os campos e chamar `save`.

A camada de service é onde as regras de negócio devem viver. O controller só orquestra — recebe o request, chama o service, devolve o response. O repository só acessa o banco. Essa separação facilita testar cada camada isoladamente.

---

## Dificuldades e como resolvi

Nenhuma dificuldade nesta task.
