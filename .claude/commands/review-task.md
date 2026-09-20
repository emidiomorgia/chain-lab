---
description: Code review automatica di un task in Verify, esito postato come nota
argument-hint: [task-id]
---

Task id: $ARGUMENTS

Il task deve essere in stato Verify. Rivedi il codice modificato rispetto a:
- il piano approvato (`--plan` del task)
- gli acceptance criteria
- la definition of done

Cerca in particolare: scostamenti dal piano non giustificati, AC dichiarati
soddisfatti ma non effettivamente coperti dal codice, edge case menzionati
nella descrizione ma non gestiti, problemi di leggibilità/coerenza con lo
stile del resto del progetto.

Posta l'esito come nota, SENZA cambiare lo status:
```
backlog task edit $ARGUMENTS --append-notes "[AI REVIEW] <esito sintetico: OK, o elenco puntuale dei problemi trovati>"
```

Poi fermati: la decisione se richiedere modifiche o approvare spetta
all'utente, non a te.
