# 005 — EquipmentRepository

- **Task:** Criar interface `EquipmentRepository`
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Criado `EquipmentRepository.java` no pacote `repository`, estendendo `JpaRepository<Equipment, Long>`.

---

## Por que foi feito assim

**Por que só uma interface vazia?** O Spring Data JPA usa o padrão de repositório: ao ver uma interface que estende `JpaRepository`, ele gera automaticamente em runtime uma implementação com os métodos CRUD completos (`findAll`, `findById`, `save`, `deleteById`, etc.). Não é necessário escrever nenhuma implementação manual para operações básicas.

**`JpaRepository<Equipment, Long>`:** os dois tipos genéricos são a entidade gerenciada (`Equipment`) e o tipo do seu `id` (`Long`). O Spring Data usa essa informação para gerar as queries corretas.

**Por que no pacote `repository`?** Separar repositórios em um pacote próprio segue o princípio de separação de responsabilidades definido no `ARCHITECTURE.md` — o acesso ao banco fica isolado, o service não conhece SQL ou JPA diretamente.

---

## O que aprendi

O Spring Data JPA implementa o padrão *Repository* do Domain-Driven Design. A "mágica" acontece em tempo de inicialização: o Spring escaneia interfaces que estendem `Repository` (ou suas subinterfaces como `JpaRepository`), gera proxies dinâmicos com a implementação e os registra como beans no contexto — por isso conseguimos injetar `EquipmentRepository` no service com `@Autowired` ou via construtor sem nunca ter escrito uma classe concreta.

Métodos customizados podem ser adicionados seguindo convenções de nomenclatura (`findBySerialNumber`, `findByStatus`) e o Spring Data gera a query automaticamente a partir do nome do método — sem SQL.

---

## Dificuldades e como resolvi

Nenhuma dificuldade nesta task.
