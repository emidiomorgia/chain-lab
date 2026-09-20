---
description: Archivia un task verificato e sincronizza i file locali/generali (dopo checkpoint 3)
argument-hint: [task-id] [slug-change]
---

Task id: $1 — change: $2

Segui "Archiviazione" e "Sync dopo ogni archiviazione" di CLAUDE.md:
1. `backlog task archive $1`
2. Aggiorna SEMPRE (locale):
   - `changes/$2/spec.md` — append di descrizione + AC del task
   - `changes/$2/changelog.md` — append del final-summary
   - `changes/$2/adr.md` — se il task referenzia una backlog decision, integra
3. Aggiorna SEMPRE `docs/changelog.md` generale con una riga
4. Valuta `docs/specs.md` e `docs/architecture.md` — solo se il task introduce
   qualcosa di architetturalmente o funzionalmente duraturo. Se in dubbio,
   chiedimi prima di scrivere, non decidere da solo
