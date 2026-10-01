# 006 — DTOs: EquipmentRequest e EquipmentResponse

- **Task:** Criar `EquipmentRequest` e `EquipmentResponse`
- **Data:** 2026-09-30
- **Referência:** `specs/001-crud-equipamentos/tasks.md`

---

## O que foi feito

Criados dois records Java no pacote `dto`:
- `EquipmentRequest` — recebe os dados de entrada da API com validações
- `EquipmentResponse` — define o que a API devolve ao cliente, com método estático `from(Equipment)`

---

## Por que foi feito assim

**Por que DTOs e não expor a entidade diretamente?** A entidade `Equipment` é um objeto de persistência — ela reflete a estrutura do banco. Expô-la diretamente na API cria acoplamento: qualquer mudança no banco (novo campo, renomeação) afeta o contrato da API. O DTO é uma camada de isolamento: o que o cliente vê é independente do que está no banco.

**Por que `record` e não classe com Lombok?** Records são imutáveis por definição — exatamente o que se quer num DTO. Menos código, sem Lombok, sem risco de mutação acidental. Disponível desde Java 16, amplamente usado em Java 21.

**`@NotBlank` vs `@NotNull`:** `@NotBlank` rejeita null, string vazia e string só com espaços — cobre os casos de campos de texto obrigatórios. `@NotNull` é usado no `status` porque o tipo é um enum, não uma String — `@NotBlank` não se aplica a tipos não textuais.

**`EquipmentResponse.from(Equipment)`:** método estático de fábrica que converte a entidade para o DTO de resposta. Centraliza a conversão em um único lugar — se o response mudar, só este método precisa ser alterado.

---

## O que aprendi

Records em Java são classes imutáveis com construtor, getters, `equals`, `hashCode` e `toString` gerados automaticamente pelo compilador. São ideais para DTOs porque um DTO não deve ser mutável — ele só transporta dados.

A separação entre DTO de entrada (`Request`) e DTO de saída (`Response`) é uma boa prática: o que o cliente envia e o que ele recebe de volta são contratos diferentes. O `id`, por exemplo, não faz sentido no request (quem cria não escolhe o id), mas está presente no response.

---

## Dificuldades e como resolvi

Nenhuma dificuldade nesta task.
