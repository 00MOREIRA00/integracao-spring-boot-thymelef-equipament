# Glossário

> Termos do domínio de negócio usados neste projeto. Sempre que um termo aparecer em specs, código ou documentação, o significado é o definido aqui.

---

## Equipamento

Item físico de TI pertencente à empresa (notebook, monitor, headset, teclado, etc.). Um equipamento é identificado pelo seu **número de série**, que é único no catálogo. Diferente de *produto*, que é uma categoria genérica — aqui cada registro representa um item físico individual.

---

## Número de série

Código único que identifica fisicamente um equipamento. Atribuído pelo fabricante ou pela empresa. Dois equipamentos nunca podem ter o mesmo número de série no catálogo.

---

## Status do equipamento

Estado operacional atual de um equipamento. Os valores possíveis são:

| Status         | Significado                                                  |
|----------------|--------------------------------------------------------------|
| `DISPONIVEL`   | Equipamento livre, pode ser alocado a um colaborador         |
| `EM_USO`       | Equipamento atualmente alocado a um colaborador ou projeto   |
| `EM_MANUTENCAO`| Equipamento fora de operação, aguardando reparo ou descarte  |

---

## Perfil de ambiente

Configuração que determina qual banco de dados e quais parâmetros a aplicação usa ao iniciar. Os perfis deste projeto são `dev` (H2 in-memory) e `prd` (PostgreSQL).

---

## Catálogo

O conjunto de todos os equipamentos registrados no sistema. É o domínio central desta aplicação.
