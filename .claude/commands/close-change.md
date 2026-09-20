---
description: Chiude una change quando tutti i task del milestone sono archiviati
argument-hint: [slug-change] [milestone-id]
---

Change: $1 — milestone: $2

Segui "Chiusura di una change" di CLAUDE.md:
1. Verifica che tutti i task del milestone $2 siano archiviati — se ne manca
   anche solo uno, fermati e dimmi quali
2. Verifica coerenza tra changes/$1/spec.md e lo stato reale del codice
3. `backlog milestone archive $2`
4. Sposta changes/$1/ in changes/archive/YYYY-MM-DD-$1/ (data odierna)
