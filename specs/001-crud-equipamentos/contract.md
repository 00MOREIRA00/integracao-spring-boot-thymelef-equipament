# Contrato: Equipment API

- **Base URL:** `/api/equipments`
- **Formato:** JSON (`Content-Type: application/json`)
- **Autenticação:** nenhuma

---

## `GET /api/equipments`

Lista todos os equipamentos cadastrados.

**Response `200 OK`**

```json
[
  {
    "id": 1,
    "name": "Notebook Dell XPS",
    "category": "notebook",
    "serialNumber": "SN-001",
    "status": "DISPONIVEL"
  }
]
```

> Retorna array vazio `[]` quando não há equipamentos.

---

## `GET /api/equipments/{id}`

Busca um equipamento pelo id.

**Path params**

| Parâmetro | Tipo | Descrição           |
|-----------|------|---------------------|
| `id`      | Long | Id do equipamento   |

**Response `200 OK`**

```json
{
  "id": 1,
  "name": "Notebook Dell XPS",
  "category": "notebook",
  "serialNumber": "SN-001",
  "status": "DISPONIVEL"
}
```

**Erros**

| Código | Quando ocorre              |
|--------|----------------------------|
| `404`  | Id não encontrado no banco |

---

## `POST /api/equipments`

Cadastra um novo equipamento.

**Request**

```json
{
  "name": "Notebook Dell XPS",
  "category": "notebook",
  "serialNumber": "SN-001",
  "status": "DISPONIVEL"
}
```

| Campo          | Tipo   | Obrigatório | Validação               |
|----------------|--------|-------------|-------------------------|
| `name`         | String | Sim         | Não pode ser vazio      |
| `category`     | String | Sim         | Não pode ser vazio      |
| `serialNumber` | String | Sim         | Não pode ser vazio      |
| `status`       | String | Sim         | Um dos valores do enum: `DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO` |

**Response `201 Created`**

```json
{
  "id": 1,
  "name": "Notebook Dell XPS",
  "category": "notebook",
  "serialNumber": "SN-001",
  "status": "DISPONIVEL"
}
```

**Erros**

| Código | Quando ocorre                        |
|--------|--------------------------------------|
| `400`  | Campo obrigatório ausente ou inválido|
| `409`  | Número de série já cadastrado        |

---

## `PUT /api/equipments/{id}`

Atualiza os dados de um equipamento existente.

**Path params**

| Parâmetro | Tipo | Descrição           |
|-----------|------|---------------------|
| `id`      | Long | Id do equipamento   |

**Request** — todos os campos são obrigatórios (substituição completa).

```json
{
  "name": "Notebook Dell XPS 15",
  "category": "notebook",
  "serialNumber": "SN-001",
  "status": "EM_MANUTENCAO"
}
```

**Response `200 OK`**

```json
{
  "id": 1,
  "name": "Notebook Dell XPS 15",
  "category": "notebook",
  "serialNumber": "SN-001",
  "status": "EM_MANUTENCAO"
}
```

**Erros**

| Código | Quando ocorre                         |
|--------|---------------------------------------|
| `400`  | Campo obrigatório ausente ou inválido |
| `404`  | Id não encontrado no banco            |
| `409`  | Número de série já pertence a outro equipamento |

---

## `DELETE /api/equipments/{id}`

Remove um equipamento do catálogo.

**Path params**

| Parâmetro | Tipo | Descrição           |
|-----------|------|---------------------|
| `id`      | Long | Id do equipamento   |

**Response `204 No Content`** — body vazio.

**Erros**

| Código | Quando ocorre              |
|--------|----------------------------|
| `404`  | Id não encontrado no banco |

---

## Formato de erro

Todos os erros seguem o mesmo envelope:

```json
{
  "status": 409,
  "message": "Número de série SN-001 já está cadastrado."
}
```
