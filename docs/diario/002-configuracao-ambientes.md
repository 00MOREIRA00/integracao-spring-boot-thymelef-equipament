# 002 — Configuração de ambientes (dev e prd)

- **Task:** Configurar `application.yml`, `application-dev.yml` e `application-prd.yml`
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

- Removido o `application.properties` gerado pelo Initializr.
- Criados três arquivos YAML no lugar:
  - `application.yml` — configuração base compartilhada por todos os perfis (nome da app e porta).
  - `application-dev.yml` — H2 in-memory, console habilitado, DDL `create-drop`, `show-sql: true`.
  - `application-prd.yml` — PostgreSQL via variáveis de ambiente, DDL `validate`, `show-sql: false`.

---

## Por que foi feito assim

**Por que três arquivos separados e não um só com condicionais?**
O Spring Boot tem suporte nativo a perfis: ao subir com `--spring.profiles.active=dev`, ele carrega o `application.yml` primeiro e depois mescla o `application-dev.yml` por cima. Isso elimina qualquer lógica condicional no código — a troca de ambiente é feita só pela flag de inicialização.

**Por que YAML e não properties?**
YAML é hierárquico, o que evita repetição de prefixos (`spring.datasource.url`, `spring.datasource.username`...). Para arquivos de configuração com muitas chaves aninhadas, a leitura fica mais clara.

**Por que `ddl-auto: create-drop` em dev e `validate` em prd?**
- `create-drop`: o Hibernate cria o schema ao subir e derruba ao encerrar. Ideal para dev — banco sempre limpo, sem precisar de scripts SQL.
- `validate`: o Hibernate apenas confere se o schema existente bate com as entidades. Em prd o schema é gerenciado separadamente (script SQL versionado); deixar o Hibernate alterar a estrutura em produção é arriscado.

**Por que `${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}` em prd?**
Credenciais não devem ser versionadas no repositório. As variáveis de ambiente são injetadas no momento da execução pelo ambiente de produção. O arquivo `application-prd.yml` é seguro para commitar porque não contém valor algum, só referências.

---

## O que aprendi

O Spring Boot usa uma convenção de nomenclatura para carregar perfis automaticamente: `application-{profile}.yml`. Não é necessário nenhuma anotação ou configuração extra — basta o arquivo existir com o nome correto e o perfil ser ativado na inicialização.

A ordem de precedência é: `application-{profile}.yml` sobrescreve `application.yml`. Então tudo que é comum (porta, nome da app) fica no base, e só o que muda por ambiente vai no arquivo de perfil.

---

## Dificuldades e como resolvi

**`application.properties` gerado pelo Initializr:** foi necessário remover o `.properties` antes de criar os `.yml` — se ambos coexistissem, o Spring Boot carregaria os dois e poderia haver conflito de propriedades.

**Console H2 não disponível no Spring Boot 4.x:** a `H2ConsoleAutoConfiguration` que registrava automaticamente o console web em versões anteriores não dispara no Spring Boot 4.x — nenhuma linha dela aparece no log de startup. A tentativa de registrar o servlet manualmente (`JakartaWebServlet`) falhou porque o H2 está com `scope: runtime` no `pom.xml`, logo suas classes não ficam disponíveis em tempo de compilação. Conclusão: o console H2 foi descontinuado ou movido no Spring Boot 4.x. A conexão com o banco funciona normalmente (confirmado pelo log do HikariPool) — o console web é apenas uma ferramenta de diagnóstico acessória e não é necessária para o desenvolvimento da API.
