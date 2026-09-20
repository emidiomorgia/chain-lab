---
description: Approva un task in Verify (Verify -> Done) e sincronizza i file locali/generali
argument-hint: [task-id] [slug-change]
---

Task id: $1 — change: $2

Precondizione: il task deve essere in stato Verify. Se è ancora In Progress
o Done, fermati e dimmelo invece di procedere.

Segui "Decisione dell'utente" e "Sync all'approvazione" di CLAUDE.md:
1. `backlog task edit $1 -s Done` — il task resta sulla board, colonna Done.
   NON eseguire `backlog task archive` qui: l'archiviazione fisica avviene
   solo con /close-change
2. Aggiorna SEMPRE (locale):
   - `changes/$2/spec.md` — append di descrizione + AC del task
   - `changes/$2/changelog.md` — append del final-summary
   - `changes/$2/adr.md` — se il task referenzia una backlog decision, integra
3. Aggiorna SEMPRE `docs/changelog.md` generale con una riga
4. Valuta `docs/specs.md` e `docs/architecture.md` — solo se il task introduce
   qualcosa di architetturalmente o funzionalmente duraturo. Se in dubbio,
   chiedimi prima di scrivere, non decidere da solo
