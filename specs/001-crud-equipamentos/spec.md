# Feature: CRUD de Equipamentos

> Fonte de verdade desta feature. Alterações aqui devem ser sinalizadas — o `tasks.md` deriva deste documento.

- **Criado em:** 2026-09-30
- **Status:** Concluído

---

## Contexto

O time de TI da Techsupply controla o inventário de equipamentos por planilha compartilhada. O processo é manual, propenso a inconsistências e sem histórico confiável. Esta feature cria a aplicação do zero: um CRUD web simples que centraliza o catálogo de equipamentos.

O foco do projeto é aprender **Spring Boot + Spring MVC + configuração de múltiplos ambientes**, não construir um sistema complexo. A API retorna JSON — sem views HTML.

---

## Requisitos

### Funcionais

- [ ] RF-01: O sistema deve permitir cadastrar um equipamento com nome, categoria, número de série e status.
- [ ] RF-02: O sistema deve listar todos os equipamentos cadastrados.
- [ ] RF-03: O sistema deve permitir editar os dados de um equipamento existente.
- [ ] RF-04: O sistema deve permitir remover um equipamento do catálogo.
- [ ] RF-05: O número de série deve ser único — o sistema deve rejeitar duplicatas com mensagem de erro.

### Não funcionais

- [ ] RNF-01: Console H2 acessível via browser no perfil `dev` (`/h2-console`).
- [ ] RNF-02: Troca entre `dev` e `prd` feita exclusivamente por configuração — sem `if` de perfil no código.
- [ ] RNF-03: Credenciais de banco em `prd` injetadas via variáveis de ambiente (nunca em texto plano versionado).

---

## Cenários de teste

**Cenário 1: Cadastro com sucesso**
- **Dado** que não existe equipamento com o número de série informado
- **Quando** `POST /api/equipments` com payload válido
- **Então** resposta `201 Created` com o equipamento criado no body

**Cenário 2: Número de série duplicado**
- **Dado** que já existe um equipamento com número de série "SN-001"
- **Quando** `POST /api/equipments` com `serialNumber: "SN-001"`
- **Então** resposta `409 Conflict` com mensagem de erro no body

**Cenário 3: Edição com sucesso**
- **Dado** que existe um equipamento com id `1`
- **Quando** `PUT /api/equipments/1` com novo status
- **Então** resposta `200 OK` com os dados atualizados

**Cenário 4: Remoção**
- **Dado** que existe um equipamento com id `1`
- **Quando** `DELETE /api/equipments/1`
- **Então** resposta `204 No Content` e equipamento não aparece mais na listagem

**Cenário 5: Console H2 acessível em dev**
- **Dado** que a aplicação subiu com `--spring.profiles.active=dev`
- **Quando** acesso `/h2-console` no browser
- **Então** consigo visualizar o esquema e consultar a tabela de equipamentos

---

## Decisões técnicas

- **REST com `@RestController`:** a API retorna JSON em todos os endpoints. Sem views HTML, sem Thymeleaf.
- **DTOs para request/response:** a entidade `Equipment` não é exposta diretamente — `EquipmentRequest` recebe os dados de entrada e `EquipmentResponse` define o que é devolvido ao cliente.
- **Enum para status:** `EquipmentStatus` como enum Java garante tipo fechado e evita strings arbitrárias no banco.
- **Constraint `@Column(unique = true)` no número de série:** o banco garante unicidade a nível de schema. O service captura a exceção e devolve `409 Conflict` com mensagem legível.
- **Handler global de erros:** `@ControllerAdvice` centraliza o mapeamento de exceções para códigos HTTP — o controller não trata erro diretamente.

Decisão sobre ambientes documentada em `docs/adr/0001-multiambiente-h2-postgres.md`.

---

## Modelo de dados

### Equipment

| Campo       | Tipo            | Obrigatório | Descrição                              |
|-------------|-----------------|-------------|----------------------------------------|
| `id`        | Long            | Sim         | Gerado automaticamente (auto-increment)|
| `name`      | String (255)    | Sim         | Nome do equipamento                    |
| `category`  | String (100)    | Sim         | Categoria (notebook, monitor, etc.)    |
| `serialNumber` | String (100) | Sim         | Número de série — único no catálogo    |
| `status`    | Enum            | Sim         | `DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO`|

### DDL gerado automaticamente

Em `dev`: `spring.jpa.hibernate.ddl-auto=create-drop` — tabela recriada a cada restart.
Em `prd`: `spring.jpa.hibernate.ddl-auto=validate` — schema deve existir previamente.

```sql
-- Script de criação para prd (executar antes de subir a aplicação)
CREATE TABLE equipment (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    category    VARCHAR(100) NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    status      VARCHAR(50)  NOT NULL
);
```

---

## Pendências

- [ ] Decidir se a remoção será física (DELETE) ou lógica (soft delete com campo `deleted_at`). Por ora: remoção física — escopo simples.
