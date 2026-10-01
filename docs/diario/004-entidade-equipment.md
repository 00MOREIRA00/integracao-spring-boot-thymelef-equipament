# 004 — Entidade Equipment

- **Task:** Criar entidade `Equipment`
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Criado `Equipment.java` no pacote `model` com as anotações JPA e Lombok. A tabela `EQUIPMENT` foi gerada automaticamente pelo Hibernate ao subir a aplicação com perfil `dev` e confirmada no H2 console.

---

## Por que foi feito assim

**`@Entity` e `@Table(name = "equipment")`:** `@Entity` diz ao JPA que essa classe representa uma tabela. `@Table` define o nome explicitamente — sem ela o Hibernate usaria o nome da classe (`Equipment`), que funcionaria, mas ser explícito evita surpresas com convenções de capitalização entre bancos.

**`@GeneratedValue(strategy = GenerationType.IDENTITY)`:** delega a geração do id para o banco (auto-increment no PostgreSQL, sequência no H2). É a estratégia mais simples e suficiente para este projeto. A alternativa `SEQUENCE` dá mais controle, mas exige configuração adicional.

**`unique = true` em `serialNumber`:** a constraint de unicidade fica no banco, não só na aplicação. Isso garante que mesmo acessos diretos ao banco (migrações, scripts, outro serviço) não quebrem a regra. O service vai capturar a exceção que o banco lança quando a constraint é violada.

**`@Enumerated(EnumType.STRING)`:** sem essa anotação o JPA usaria `EnumType.ORDINAL` por padrão, gravando 0, 1 ou 2 no banco. Se um novo status fosse adicionado no meio do enum, todos os valores existentes mudariam de índice — corrompendo os dados. Com `STRING`, o que vai pro banco é o nome (`DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO`), que é estável independente da ordem de declaração.

**Lombok (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`):** elimina getters, setters, `equals`, `hashCode` e `toString` que o JPA e o Spring precisam mas que são boilerplate puro. `@Builder` facilita a criação de instâncias nos testes e no service sem precisar de construtores longos.

---

## O que aprendi

O Hibernate com `ddl-auto: create-drop` cria o schema automaticamente a partir das anotações da entidade ao iniciar e derruba tudo ao encerrar. Isso é visível no H2 console: a tabela `EQUIPMENT` aparece com as colunas exatamente como declaradas na entidade, incluindo a constraint `UNIQUE` em `SERIAL_NUMBER`.

O nome das colunas segue a convenção do Hibernate: camelCase vira snake_case (`serialNumber` → `SERIAL_NUMBER`). Isso pode ser customizado com `@Column(name = "...")`, como foi feito aqui para deixar o mapeamento explícito.

---

## Dificuldades e como resolvi

Nenhuma dificuldade nesta task.
