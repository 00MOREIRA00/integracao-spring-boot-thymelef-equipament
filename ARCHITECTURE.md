# Arquitetura

> Este arquivo descreve o **estado atual** da arquitetura. Decisões passadas ficam em `docs/adr/`. Decisões específicas de uma feature ficam no `spec.md` dela.

## Visão geral

API REST monolítica com Spring MVC. O cliente (Postman, frontend externo, etc.) faz requisições HTTP ao controller, que delega ao service, que usa o repository para persistir no banco. O controller retorna JSON — não há views HTML nem template engine.

```
  Cliente HTTP (Postman / frontend)
        │
        ▼
  ┌─────────────────────────────────────────┐
  │           Spring MVC (Tomcat)           │
  │                                         │
  │  @RestController → Service → Repository │
  │       │                                 │
  │   Jackson (serializa JSON)              │
  └─────────────────────────────────────────┘
                        │
          ┌─────────────┴──────────────┐
          ▼ (dev)                      ▼ (prd)
        H2 in-memory              PostgreSQL
```

## Stack

| Camada          | Tecnologia          | Versão  |
|-----------------|---------------------|---------|
| Linguagem       | Java                | 17+     |
| Framework       | Spring Boot         | 3.x     |
| MVC / Web       | Spring MVC          | —       |
| Serialização    | Jackson (embutido)  | —       |
| ORM             | Spring Data JPA     | —       |
| Banco dev       | H2                  | —       |
| Banco prd       | PostgreSQL          | 15+     |
| Build           | Maven               | 3.8+    |

## Estrutura de pacotes

```
src/main/java/com/techsupply/catalog/
├── controller/     # recebe requisição HTTP, chama service, retorna JSON (@RestController)
├── service/        # regras de negócio — aqui mora a lógica da aplicação
├── repository/     # interface JPA — acesso ao banco
├── model/          # entidades JPA e enums do domínio
├── dto/            # objetos de entrada/saída da API (request/response)
└── exception/      # exceções de negócio e handler global (@ControllerAdvice)

src/main/resources/
├── application.yml          # configuração base (porta, nome da app)
├── application-dev.yml      # H2, console habilitado, DDL auto-create
└── application-prd.yml      # PostgreSQL via variáveis de ambiente, DDL validate
```

## Padrões adotados

- **Separação de responsabilidades:** controller não acessa o repository diretamente — sempre passa pelo service.
- **Configuração por perfil:** toda diferença entre dev e prd fica nos arquivos `application-{profile}.yml`. Sem `if` de perfil no código Java.
- **Credenciais via variável de ambiente:** o `application-prd.yml` referencia `${DB_URL}`, `${DB_USERNAME}` e `${DB_PASSWORD}` — nunca valores em texto plano versionados.

## Integrações externas

Nenhuma neste momento.

## Limites e restrições conhecidas

- Sem autenticação — aplicação interna de rede fechada.
- Sem paginação na listagem inicial — aceitável enquanto o volume for pequeno.
- H2 é in-memory: dados do ambiente dev são perdidos ao reiniciar. Intencional para dev, documentado em `docs/adr/0001-multiambiente-h2-postgres.md`.
- Sem Thymeleaf — API REST pura; cliente consome JSON. Decisão tomada no início do projeto.
