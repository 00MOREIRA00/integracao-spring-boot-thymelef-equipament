# Instruções para agentes de IA

## Antes de começar qualquer tarefa

- Leia `ARCHITECTURE.md` se a tarefa envolver decisão técnica nova ou mudança estrutural.
- Leia `TESTING.md` antes de escrever qualquer teste.
- Leia `GLOSSARY.md` se encontrar termos de domínio desconhecidos.
- **Não leia todas as pastas de `specs/`** — só a da feature em questão.

## Ao criar uma feature nova

1. Crie `specs/NNN-nome-da-feature/spec.md` seguindo o template em `docs/SDD.md`.
2. Gere `tasks.md` a partir do `spec.md`.
3. Só crie `test-plan.md` se os cenários de teste forem complexos.
4. Esta aplicação é uma API REST — leia o `contract.md` da feature antes de implementar o controller.

## Ao implementar uma feature existente

- Leia `spec.md` antes de escrever código.
- Marque as tarefas em `tasks.md` conforme forem concluídas.
- Não altere o `spec.md` sem sinalizar a mudança.

## Diário de aprendizado (obrigatório ao concluir cada task)

Este é um projeto de estudo. A cada task concluída, crie um arquivo em `docs/diario/` seguindo o template `docs/diario/_template.md`.

**Nomenclatura:** `NNN-nome-curto-da-task.md` — onde `NNN` é o número sequencial da entrada no diário (001, 002, 003...), não o número da task.

**O arquivo deve conter:**
1. **O que foi feito** — o que mudou no código de forma objetiva.
2. **Por que foi feito assim** — decisões técnicas tomadas e alternativas descartadas.
3. **O que aprendi** — conceito novo que ficou claro após implementar (comportamento do Spring, anotação, etc.).
4. **Dificuldades e como resolvi** — problemas encontrados e como foram superados; dúvidas em aberto.

**Quando criar:** imediatamente após marcar a task como concluída em `tasks.md`, antes de avançar para a próxima.

**Tamanho:** não precisa ser longo. Uma frase por seção já tem valor. O objetivo é registrar o raciocínio enquanto está fresco, não escrever um artigo.

## Convenções do projeto

- **Idioma do código:** identificadores em inglês (`equipmentName`, `serialNumber`), comentários e documentação em português.
- **Padrão de commits:** Conventional Commits — `feat:`, `fix:`, `chore:`, `docs:`.
- **Profiles Spring:** nunca usar `if (profile.equals("dev"))` no código Java — toda diferença entre ambientes fica nos arquivos `application-{profile}.yml`.
- **Credenciais:** nunca commitar credenciais de banco. Em `prd`, usar variáveis de ambiente (`${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`).
- **Camadas:** controller não acessa o repositório diretamente — sempre passa pelo service.
- **DTOs:** nunca expor a entidade JPA diretamente na API — usar `*Request` para entrada e `*Response` para saída.

## O que não fazer

- Não crie arquivos fora da estrutura descrita em `docs/SDD.md`.
- Não edite um ADR existente em `docs/adr/` — mudanças viram um novo ADR.
- Não apague pastas de `specs/` de features concluídas.
- Não adicione lógica de negócio no controller — isso pertence ao service.
- Não habilite o console H2 no perfil `prd`.
- Não exponha a entidade JPA diretamente no response — sempre usar DTOs.

## Referência da estrutura

Para entender o propósito de cada arquivo, consulte `docs/SDD.md`.
