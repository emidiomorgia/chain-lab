---
description: Implementa un task pianificato, popola note/final-summary e lo invia in Verify
argument-hint: [task-id]
---

Task id: $ARGUMENTS

Segui il passo 3 del ciclo in CLAUDE.md:
1. Prima di tutto: `backlog task edit $ARGUMENTS -s "In Progress"`
2. Implementa seguendo il piano già approvato sul task $ARGUMENTS
3. A fine implementazione popola:
   - `backlog task edit $ARGUMENTS --append-notes "[AGENT] <cosa è stato fatto davvero>"`
   - `backlog task edit $ARGUMENTS --final-summary "<voce di changelog per questo task>"`
   - `backlog task edit $ARGUMENTS --check-ac <n>` per ogni acceptance criterion soddisfatto
   - `backlog task edit $ARGUMENTS --check-dod <n>` per ogni definition of done verificata
4. Mostrami diff/codice rilevante, le note e gli AC/DoD spuntati
5. SUBITO DOPO, senza aspettare mia conferma, esegui:
   `backlog task edit $ARGUMENTS -s Verify`
6. Avvisami esplicitamente che il task è in Verify, con un breve riepilogo
   di cosa è stato fatto, e ricordami le opzioni: /review-task per una code
   review automatica, /request-changes per rimandarlo indietro,
   /complete-task per approvarlo
