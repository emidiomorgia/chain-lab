---
description: Genera il piano di implementazione per un task esistente (checkpoint 2)
argument-hint: [task-id]
---

Task id: $ARGUMENTS

Segui il passo 2 del ciclo in CLAUDE.md:
1. Leggi il task $ARGUMENTS (descrizione, AC, DoD) e lo stato attuale del
   codice rilevante nel repo
2. Scrivi il piano implementativo: `backlog task edit $ARGUMENTS --plan "..."`
3. Mostrami il piano, poi FERMATI

> CHECKPOINT 2 — aspetto conferma o richiesta di modifica. Se ti dico che
> eseguo io il task, il piano resta come guida e non procedi con /implement-task.
