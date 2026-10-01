# CATL-011 — Catálogo de Equipamentos de TI

## Contexto

A **Techsupply** gerencia um inventário interno de equipamentos de TI (notebooks, monitores, headsets, etc.) hoje controlado por planilha. O time de TI precisa de uma aplicação web simples para registrar, consultar, editar e remover equipamentos.

A aplicação roda localmente com H2 durante o desenvolvimento e em um servidor Linux com PostgreSQL em produção. A troca de ambiente não deve exigir nenhuma alteração no código.

---

## Stack

| Item         | Tecnologia                        |
|--------------|-----------------------------------|
| Framework    | Spring Boot + Spring MVC          |
| Template     | Thymeleaf                         |
| Persistência | Spring Data JPA                   |
| Banco dev    | H2 (in-memory, sobe com a app)    |
| Banco prd    | PostgreSQL (já provisionado)      |
| Perfis       | `dev` e `prd`                     |

---

## Modelo de dados

| Campo       | Tipo        | Observação                         |
|-------------|-------------|-------------------------------------|
| id          | Long        | gerado automaticamente              |
| nome        | String      | obrigatório                         |
| categoria   | String      | ex.: notebook, monitor, headset     |
| numeroSerie | String      | obrigatório, único                  |
| status      | String/Enum | disponível / em uso / em manutenção |

---

## Requisitos funcionais

- **RF-01** — Cadastrar equipamento com nome, categoria, número de série e status.
- **RF-02** — Listar todos os equipamentos cadastrados.
- **RF-03** — Editar os dados de um equipamento existente.
- **RF-04** — Remover um equipamento do catálogo.
- **RF-05** — Número de série deve ser único.

## Requisitos não funcionais

- **RNF-01** — Console do H2 acessível via browser em `dev`.
- **RNF-02** — Troca entre `dev` e `prd` feita exclusivamente por configuração externa (sem `if` no código).
- **RNF-03** — Em `prd`, conectar no PostgreSQL já existente.

---

## Restrições

- Views com Thymeleaf (sem frameworks frontend).
- Banco de `dev` sobe junto com a aplicação, sem instalação adicional.
- Credenciais de produção não devem ser versionadas no Git.

---

## Critérios de aceitação

```gherkin
Dado que a aplicação está rodando com o perfil "dev"
Quando acesso o console do H2 no browser
Então consigo ver o esquema criado automaticamente

Dado que cadastrei um equipamento com número de série "SN-001"
Quando tento cadastrar outro com o mesmo número de série
Então recebo mensagem de erro informando que o número de série já existe

Dado que existem equipamentos cadastrados
Quando acesso a página de listagem
Então vejo todos com nome, categoria e status

Dado que a aplicação está rodando com o perfil "prd"
Quando ela inicia
Então conecta no PostgreSQL sem nenhuma alteração de código
```

---

## Definition of Done

- Aplicação sobe sem erros com `--spring.profiles.active=dev`
- Aplicação sobe sem erros com `--spring.profiles.active=prd` (PostgreSQL disponível)
- CRUD funcional via browser
- Nenhum `if (profile.equals("dev"))` no código Java — apenas configuração

---

## Perguntas para guiar o desenvolvimento

1. Como o Spring Boot sabe qual configuração usar em cada ambiente? O que precisa estar no nome do arquivo?
2. Como garantir que o H2 só esteja ativo em `dev` e não interfira em `prd`?
3. O que acontece se subir com perfil `prd` mas o PostgreSQL não estiver acessível?
4. Onde ficam as credenciais de produção? Devem estar num arquivo versionado no Git?

---

## Como executar

```bash
# Ambiente de desenvolvimento
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Ambiente de produção
./mvnw spring-boot:run -Dspring-boot.run.profiles=prd
```

> Em `dev`, o console do H2 estará disponível em: `http://localhost:8080/h2-console`
