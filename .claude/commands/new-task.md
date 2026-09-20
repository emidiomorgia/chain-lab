---
description: Intervista per un nuovo task Backlog.md nella change corrente (checkpoint 1)
argument-hint: [slug-change] [breve descrizione]
---

Change di riferimento: "$1". Descrizione iniziale: "$2"

Segui il "Ciclo per singolo task" di CLAUDE.md, passo 1:
1. Fai le domande necessarie a chiarire scope, comportamento atteso ed edge case
   — non dare per scontato nulla che non ti ho detto esplicitamente
2. Quando hai chiarezza sufficiente, crea il task:
   `backlog task create "<titolo>" -d "<descrizione>" --ac "<criterio1>" --ac "<criterio2>" --dod "<verifica1>"`
   assegnato al milestone della change "$1"
3. Mostrami descrizione, acceptance criteria e definition of done, poi FERMATI

> CHECKPOINT 1 — aspetto la mia conferma o richiesta di modifica prima che
> tu proceda a qualunque altro passo.
