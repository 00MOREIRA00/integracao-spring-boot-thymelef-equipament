# Política de Testes

> Este arquivo define **como** testar, não **o quê** testar. Os casos de teste de cada feature ficam no `test-plan.md` dela (ou como seção no `spec.md`).

## Framework e ferramentas

- Framework de teste: JUnit 5 (via `spring-boot-starter-test`)
- Testes de integração web: `MockMvc`
- Banco de teste: H2 in-memory (perfil `test`)
- Cobertura: relatório com `./mvnw test` — gerado em `target/site/jacoco/`

## Pirâmide de testes

```
        ▲
       /E2E\         [não adotado neste projeto — escopo pequeno]
      /──────\
     /Integração\    [controller + service + repository + H2 real]
    /────────────\
   /  Unitários   \  [service isolado com mock do repository]
  /────────────────\
```

| Nível       | O que testa                                          | Quando rodar    |
|-------------|------------------------------------------------------|-----------------|
| Unitário    | Service isolado (Mockito para o repository)          | A cada push     |
| Integração  | Fluxo HTTP completo via MockMvc com H2               | A cada push     |

## Convenções de nomenclatura

```
# Arquivo de teste ao lado do arquivo testado
src/main/java/.../service/EquipmentService.java
src/test/java/.../service/EquipmentServiceTest.java

# Integração: sufixo IT
src/test/java/.../controller/EquipmentControllerIT.java

# Nome do método: deve_[comportamento]_quando_[condição]
void deve_salvar_equipamento_quando_dados_validos()
void deve_lancar_excecao_quando_numero_serie_duplicado()
```

## O que mockar e o que não mockar

- **Mockar:** repository nos testes unitários de service.
- **Não mockar:** banco de dados nos testes de integração — usar H2 configurado no perfil `test`.
- **Nunca mockar:** o service nos testes de integração — isso invalida o teste.

## Como rodar

```bash
# Todos os testes
./mvnw test

# Com relatório de cobertura
./mvnw test jacoco:report
# Abrir: target/site/jacoco/index.html
```

## Onde encontrar os testes existentes

| O que você quer saber                      | Onde olhar                                      |
|--------------------------------------------|-------------------------------------------------|
| Cenários que uma feature deve cobrir       | `specs/NNN-feature/test-plan.md`                |
| Testes já implementados                    | `src/test/java/` — mesma estrutura de pacotes   |
| Se um caso de borda está coberto           | Buscar nos arquivos `*Test.java` ou `*IT.java`  |
