---
description: Implementa un task già pianificato e popola note/final-summary (checkpoint 3)
argument-hint: [task-id]
---

Task id: $ARGUMENTS

Segui il passo 3 del ciclo in CLAUDE.md:
1. Implementa seguendo il piano già approvato sul task $ARGUMENTS
2. A fine implementazione popola:
   - `backlog task edit $ARGUMENTS --append-notes "<cosa è stato fatto davvero>"`
   - `backlog task edit $ARGUMENTS --final-summary "<voce di changelog per questo task>"`
   - `backlog task edit $ARGUMENTS --check-ac <n>` per ogni acceptance criterion soddisfatto
   - `backlog task edit $ARGUMENTS --check-dod <n>` per ogni definition of done verificata
3. Mostrami diff/codice rilevante, le note e gli AC/DoD spuntati, poi FERMATI

> CHECKPOINT 3 — aspetto conferma esplicita prima di archiviare. Non lanciare
> /archive-task da solo per nessun motivo.
