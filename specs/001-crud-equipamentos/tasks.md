# Tasks: CRUD de Equipamentos

> Este é o único arquivo desta pasta pensado para ser editado durante a implementação. Marque as tarefas conforme forem concluídas. O `spec.md` deve permanecer estável.

---

## Setup do projeto

- [x] Criar projeto Spring Boot via [start.spring.io](https://start.spring.io) com dependências: Spring Web, Spring Data JPA, H2, PostgreSQL Driver, Validation, Lombok
- [x] Configurar `application.yml` com propriedades base (nome da app, porta)
- [x] Configurar `application-dev.yml` com H2, console habilitado e DDL `create-drop`
- [x] Configurar `application-prd.yml` com PostgreSQL via variáveis de ambiente e DDL `validate`
- [x] Validar que a aplicação sobe sem erros nos dois perfis

## Modelagem

- [x] Criar enum `EquipmentStatus` com valores `DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO`
- [x] Criar entidade `Equipment` com os campos do `spec.md` e constraint `unique` em `serialNumber`
- [x] Criar interface `EquipmentRepository` estendendo `JpaRepository`
- [x] Criar `EquipmentRequest` (DTO de entrada) com validações (`@NotBlank`, etc.)
- [x] Criar `EquipmentResponse` (DTO de saída) com os campos que a API expõe

## Implementação

- [x] Criar `EquipmentService` com métodos: `findAll`, `findById`, `create`, `update`, `delete`
- [x] Implementar validação de número de série duplicado no service (capturar `DataIntegrityViolationException` e lançar exceção de negócio)
- [x] Criar `GlobalExceptionHandler` com `@ControllerAdvice` mapeando exceções para status HTTP
- [x] Criar `EquipmentController` (`@RestController`) com endpoints do `contract.md`

## Testes

- [x] Teste unitário: `EquipmentServiceTest` — cadastro com sucesso (mock do repository)
- [x] Teste unitário: `EquipmentServiceTest` — número de série duplicado lança exceção de negócio
- [x] Teste de integração: `EquipmentControllerIT` — `GET /api/equipments` retorna `200` com lista
- [x] Teste de integração: `EquipmentControllerIT` — `POST /api/equipments` retorna `201` com body
- [x] Teste de integração: `EquipmentControllerIT` — `POST` com número de série duplicado retorna `409`
- [x] Teste de integração: `EquipmentControllerIT` — `DELETE /api/equipments/{id}` retorna `204`

## Revisão

- [x] Validar que não existe nenhum `if (profile...)` no código Java
- [x] Validar que o console H2 está acessível em `dev` e ausente em `prd`
- [x] Validar que as credenciais de banco não estão hardcoded em nenhum arquivo versionado
- [x] Marcar `spec.md` como `Concluído`
