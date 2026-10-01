# 010 — Testes unitários e de integração

- **Task:** Testes do `EquipmentService` e `EquipmentController`
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Criados dois arquivos de teste:

- `EquipmentServiceTest` — testes unitários com Mockito (repository mockado):
  - Cadastro com dados válidos retorna `EquipmentResponse` correto
  - Número de série duplicado lança `SerialNumberAlreadyExistsException`

- `EquipmentControllerIT` — testes de integração com MockMvc + H2:
  - `GET /api/equipments` retorna `200` com lista vazia
  - `POST /api/equipments` retorna `201` com body
  - `POST` com número de série duplicado retorna `409`
  - `DELETE /api/equipments/{id}` retorna `204`

Todos os testes passaram após downgrade do Spring Boot 4.1.1 para 3.4.5.

---

## Por que foi feito assim

**`@ExtendWith(MockitoExtension.class)` no unitário:** ativa o Mockito sem precisar subir o contexto do Spring. O teste roda rápido — sem banco, sem servidor. O repository é um mock criado pelo Mockito; o service é instanciado com esse mock injetado via `@InjectMocks`.

**`@SpringBootTest` + `@AutoConfigureMockMvc` no de integração:** sobe o contexto completo do Spring com banco H2 em memória. O `MockMvc` simula requisições HTTP sem abrir uma porta real — mais rápido que subir um servidor de verdade, e testa o fluxo completo (controller → service → repository → banco).

**`@BeforeEach` com `repository.deleteAll()`:** garante que cada teste começa com o banco limpo. Sem isso, a ordem de execução dos testes importaria — o que é um problema grave porque testes devem ser independentes.

**Downgrade para Spring Boot 3.4.5:** o Spring Boot 4.1.1 removeu `MockMvc`, `TestRestTemplate` e `@AutoConfigureMockMvc` da infraestrutura de testes. Para o objetivo deste projeto de estudo — aprender MVC e múltiplos ambientes — o 3.4.5 é mais adequado: estável, bem documentado e com toda a infraestrutura de testes web disponível.

---

## O que aprendi

A diferença entre teste unitário e de integração na prática:
- **Unitário:** isola a unidade testada (service) com mocks. Rápido. Testa regras de negócio.
- **Integração:** testa o sistema como um todo — controller recebe o request, passa pelo service, persiste no banco, e o response volta com os dados corretos. Mais lento, mas dá confiança real de que o sistema funciona.

O `MockMvc` não abre uma porta TCP — ele simula o pipeline do Spring MVC internamente. Por isso é mais rápido que subir um servidor real, mas ainda cobre o ciclo completo de serialização, validação, controller, service e banco.

---

## Dificuldades e como resolvi

**Spring Boot 4.1.1 não tem suporte a MockMvc/TestRestTemplate:** após investigar os JARs do `spring-boot-test` e `spring-boot-test-autoconfigure`, confirmamos que a infraestrutura de testes web foi removida na versão 4.x. A solução foi fazer downgrade para Spring Boot 3.4.5, onde tudo funciona conforme esperado.
