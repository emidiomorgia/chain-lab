---
description: Chiude una change - archivia i task Done del milestone e sposta la cartella
argument-hint: [slug-change] [milestone-id]
---

Change: $1 — milestone: $2

Segui "Chiusura di una change" di CLAUDE.md:
1. Elenca i task del milestone $2 e verifica che siano TUTTI in stato Done
   (usa `backlog task list --plain` filtrando per milestone: se non conosci
   la sintassi esatta del filtro nella versione installata, controlla
   `backlog task list --help`). Se anche solo uno non è Done, fermati e
   dimmi quali mancano — non procedere
2. Verifica coerenza tra changes/$1/spec.md e lo stato reale del codice
3. Per ogni task Done del milestone: `backlog task archive <id>` — a questo
   punto i task lasciano la board (finiscono in backlog/completed/, non è
   un errore se il nome non è "archive")
4. `backlog milestone archive $2`
5. Sposta changes/$1/ in changes/archive/YYYY-MM-DD-$1/ (data odierna)
