---
description: Avvia una nuova change spec-driven (milestone Backlog.md + cartella changes/)
argument-hint: [nome-change]
---

Stai avviando una nuova change: "$ARGUMENTS".

Segui la sezione "Avvio di una change" di CLAUDE.md:
1. Crea il milestone: `backlog milestone add "$ARGUMENTS"`
2. Crea la cartella `changes/<slug-change>/` (slug kebab-case derivato dal nome)
   con `adr.md`, `spec.md`, `changelog.md` — solo intestazione, verranno
   popolati dal sync man mano che i task vengono archiviati
3. Mostrami milestone-id e slug creati, poi FERMATI: non iniziare nessun
   task finché non te lo chiedo esplicitamente con /new-task
