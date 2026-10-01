# Catálogo de Equipamentos de TI — Techsupply

## O que é

Aplicação web interna para o time de TI da Techsupply gerenciar o inventário de equipamentos (notebooks, monitores, headsets, etc.). Substitui o controle por planilha, centralizando cadastro, consulta, edição e remoção de equipamentos.

Projeto de estudo focado em **Spring Boot + Spring MVC**, configuração de múltiplos ambientes (dev/prd) e persistência com JPA.

## Stack

- Linguagem: Java 17+
- Framework: Spring Boot + Spring MVC
- Template engine: Thymeleaf
- Persistência: Spring Data JPA (Hibernate)
- Banco dev: H2 (in-memory, sobe com a aplicação)
- Banco prd: PostgreSQL
- Build: Maven

## Como rodar

### Pré-requisitos

- Java 17+
- Maven 3.8+ (ou usar o wrapper `./mvnw`)
- PostgreSQL instalado e rodando (apenas para perfil `prd`)

### Ambiente de desenvolvimento (H2)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Console H2 disponível em: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:techsupply`
- Usuário: `sa` / Senha: (vazio)

### Ambiente de produção (PostgreSQL)

```bash
# Configure as variáveis de ambiente antes de rodar
export DB_URL=jdbc:postgresql://localhost:5432/techsupply
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha

./mvnw spring-boot:run -Dspring-boot.run.profiles=prd
```

### Rodando os testes

```bash
./mvnw test
```

## Estrutura do repositório

```
README.md           # este arquivo — visão de produto e onboarding
CLAUDE.md           # instruções para agentes de IA navegarem o repo
ARCHITECTURE.md     # arquitetura atual do sistema
TESTING.md          # convenções e política de testes
GLOSSARY.md         # termos do domínio de negócio

docs/
├── SDD.md          # guia da estrutura de documentação
├── adr/            # decisões técnicas arquivadas (imutável)
└── desafio-catalogo-equipamentos.md  # enunciado original do desafio

specs/              # uma pasta por feature, com spec e tasks
└── 001-crud-equipamentos/

src/
└── main/
    ├── java/
    └── resources/
        ├── application.yml          # configuração base
        ├── application-dev.yml      # sobrescreve para dev (H2)
        └── application-prd.yml      # sobrescreve para prd (PostgreSQL)
```

Para detalhes de arquitetura, veja [ARCHITECTURE.md](ARCHITECTURE.md).
Para a convenção de testes, veja [TESTING.md](TESTING.md).
Para entender a estrutura de documentação, veja [docs/SDD.md](docs/SDD.md).
