# 0001 — Configuração de múltiplos ambientes com H2 (dev) e PostgreSQL (prd)

> ADRs são imutáveis. Se a decisão mudar, crie um novo ADR referenciando este.

- **Data:** 2026-09-30
- **Status:** Aceita
- **Decisores:** time de estudo

---

## Contexto

A aplicação precisa rodar em dois ambientes: local (desenvolvimento) e servidor da empresa (produção). Em desenvolvimento, instalar e configurar um PostgreSQL seria uma barreira desnecessária — o foco do estudo é Spring Boot e MVC, não operação de banco. Em produção, o PostgreSQL já está provisionado e é o padrão da empresa.

---

## Decisão

Usar Spring Profiles (`dev` e `prd`) para isolar as configurações de banco de dados em arquivos separados (`application-dev.yml` e `application-prd.yml`). Em `dev`, usar H2 in-memory, que sobe automaticamente com a aplicação sem instalação adicional. Em `prd`, usar PostgreSQL com credenciais injetadas via variáveis de ambiente.

---

## Alternativas consideradas

| Alternativa | Por que foi descartada |
|---|---|
| PostgreSQL nos dois ambientes via Docker | Adiciona complexidade de setup (Docker) que distrai do objetivo de aprendizado |
| Um único `application.properties` com comentários | Não escala — obriga edição manual a cada troca de ambiente e é propensa a erro |
| Feature flags no código Java | Viola o princípio de "sem `if` de perfil no código" — configuração pertence ao ambiente, não ao código |

---

## Consequências

- **Positivo:** troca de ambiente é feita com uma flag (`--spring.profiles.active=dev|prd`), sem alterar código.
- **Positivo:** banco dev sobe junto com a aplicação — zero setup extra.
- **Negativo / tradeoff:** H2 é in-memory; dados do ambiente dev são perdidos ao reiniciar. Intencional e aceitável para estudo.
- **Negativo / tradeoff:** dialetos SQL podem diferir entre H2 e PostgreSQL. Mitigado usando JPQL/Spring Data em vez de queries nativas.
- **O que isso afeta:** `ARCHITECTURE.md` foi atualizado para refletir os dois bancos e a estratégia de perfis.
