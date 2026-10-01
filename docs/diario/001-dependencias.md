# 001 — Dependências do projeto

- **Task:** Setup do projeto — dependências Maven
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Projeto criado via Spring Initializr com Spring Boot 4.1.1 e Java 21. O `pom.xml` gerado teve dependências incorretas que foram corrigidas manualmente:

- `spring-boot-h2console` → removido (não existe como artefato Maven)
- `spring-boot-starter-webmvc` → substituído por `spring-boot-starter-web`
- `spring-boot-starter-data-jpa-test`, `spring-boot-starter-validation-test`, `spring-boot-starter-webmvc-test` → substituídos por um único `spring-boot-starter-test`
- `artifactId` renomeado de `thymeleaf-equipamentos` para `equipamentos-api`

Dependências finais: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `h2` (runtime), `postgresql` (runtime), `lombok` (optional), `spring-boot-starter-test` (test).

---

## Por que foi feito assim

**`spring-boot-starter-web` e não `spring-boot-starter-webmvc`:** o starter correto para Spring MVC com Tomcat embutido é o `web`. O `webmvc` não existe como starter independente — é um módulo interno do Spring Framework, não um starter do Spring Boot.

**Console H2 sem dependência própria:** o console do H2 não precisa de uma dependência separada. Ele já vem dentro do artefato `com.h2database:h2` e é ativado via propriedade `spring.h2.console.enabled=true` no `application-dev.yml`. Tentar adicionar `spring-boot-h2console` como dependência Maven causaria falha no build.

**Um único `spring-boot-starter-test`:** ele já inclui JUnit 5, Mockito, MockMvc e AssertJ — tudo o que é necessário para testes unitários e de integração. Starters de teste separados por tecnologia (`data-jpa-test`, `webmvc-test`) não existem no Maven; o Initializr os gerou com nomes inválidos.

---

## O que aprendi

O Spring Initializr às vezes gera nomes de artefatos incorretos, especialmente para dependências menos comuns ou quando combinações incomuns são selecionadas. Antes de rodar o projeto pela primeira vez, vale conferir se todos os `artifactId` no `pom.xml` existem de fato no Maven Central.

A diferença entre **starter** e **módulo** do Spring: um starter (`spring-boot-starter-*`) é um artefato Maven que agrega dependências prontas para uso. Um módulo do Spring Framework (`spring-webmvc`, `spring-data-jpa`) é a biblioteca em si. O Spring Boot usa starters para facilitar o setup — você nunca referencia o módulo diretamente no `pom.xml` quando usa Spring Boot.

---

## Dificuldades e como resolvi

O Initializr gerou um `pom.xml` com artefatos que não existem no Maven — provavelmente por selecionar opções que geraram combinações inválidas. A identificação foi feita comparando os `artifactId` gerados com os starters oficiais da documentação do Spring Boot. A correção foi direta: substituir pelos nomes corretos e consolidar os três starters de test em um único `spring-boot-starter-test`.
