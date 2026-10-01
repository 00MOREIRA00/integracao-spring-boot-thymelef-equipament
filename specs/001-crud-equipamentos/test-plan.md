# Test Plan: CRUD de Equipamentos

---

## Escopo

**O que está sendo testado:**
- `EquipmentService` — regras de negócio (cadastro, validação de duplicata, edição, remoção)
- `EquipmentController` — fluxo HTTP completo via MockMvc (status codes, body JSON)

**O que fica fora deste plano:**
- Configuração de perfis (dev/prd) — validada manualmente ao subir a aplicação
- Performance — fora do escopo deste projeto

---

## Estratégia

| Nível       | Ferramenta                 | O que cobre                                             |
|-------------|----------------------------|---------------------------------------------------------|
| Unitário    | JUnit 5 + Mockito          | `EquipmentService` com repository mockado               |
| Integração  | JUnit 5 + MockMvc + H2     | Fluxo HTTP completo — request → controller → banco (H2) |

---

## Casos de teste — Unitários (`EquipmentServiceTest`)

### Caso 1: Cadastro com dados válidos

- **Pré-condição:** repository não contém equipamento com o número de série informado
- **Entrada:** `EquipmentRequest` com todos os campos preenchidos
- **Resultado esperado:** `repository.save()` é chamado uma vez; retorna `EquipmentResponse` com os dados salvos

### Caso 2: Número de série duplicado

- **Pré-condição:** `repository.save()` lança `DataIntegrityViolationException`
- **Entrada:** `EquipmentRequest` com número de série já existente
- **Resultado esperado:** service lança `SerialNumberAlreadyExistsException`

### Caso 3: Remoção de equipamento existente

- **Pré-condição:** `repository.findById(1L)` retorna um `Optional` com o equipamento
- **Entrada:** `delete(1L)`
- **Resultado esperado:** `repository.deleteById(1L)` é chamado uma vez

### Caso 4: Busca por id inexistente

- **Pré-condição:** `repository.findById(99L)` retorna `Optional.empty()`
- **Entrada:** `findById(99L)`
- **Resultado esperado:** service lança `EquipmentNotFoundException`

---

## Casos de teste — Integração (`EquipmentControllerIT`)

### Caso 5: Listagem de equipamentos

- **Pré-condição:** banco H2 vazio
- **Request:** `GET /api/equipments`
- **Resultado esperado:** `200 OK`, body `[]`

### Caso 6: Cadastro com sucesso

- **Pré-condição:** banco H2 vazio
- **Request:** `POST /api/equipments` com payload válido
- **Resultado esperado:** `201 Created`, body com `id` gerado e campos enviados

```json
// Payload de entrada
{
  "name": "Notebook Dell XPS",
  "category": "notebook",
  "serialNumber": "SN-001",
  "status": "DISPONIVEL"
}

// Response esperada
{
  "id": 1,
  "name": "Notebook Dell XPS",
  "category": "notebook",
  "serialNumber": "SN-001",
  "status": "DISPONIVEL"
}
```

### Caso 7: Cadastro com número de série duplicado

- **Pré-condição:** equipamento com `serialNumber = "SN-001"` já existe no banco
- **Request:** `POST /api/equipments` com `serialNumber: "SN-001"`
- **Resultado esperado:** `409 Conflict`, body com mensagem de erro

### Caso 8: Edição com sucesso

- **Pré-condição:** equipamento com `id = 1` existe no banco com `status = DISPONIVEL`
- **Request:** `PUT /api/equipments/1` com `status: "EM_MANUTENCAO"`
- **Resultado esperado:** `200 OK`, body com `status: "EM_MANUTENCAO"`

### Caso 9: Remoção com sucesso

- **Pré-condição:** equipamento com `id = 1` existe no banco
- **Request:** `DELETE /api/equipments/1`
- **Resultado esperado:** `204 No Content`, body vazio

### Caso 10: Busca por id inexistente

- **Pré-condição:** banco H2 vazio
- **Request:** `GET /api/equipments/99`
- **Resultado esperado:** `404 Not Found`

---

## Dados e fixtures

Os testes de integração usam H2 configurado no perfil `test`. Cada teste configura seu próprio estado via `@BeforeEach` para ser independente.

```java
@BeforeEach
void setup() {
    repository.deleteAll();
}
```

---

## Critério de aceite

- [ ] Todos os casos acima passam com `./mvnw test`.
- [ ] Nenhum teste depende da ordem de execução.
- [ ] Nenhuma credencial real aparece nos arquivos de configuração de teste.
