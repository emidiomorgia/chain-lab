---
description: Registra un commento del reviewer su un task in Verify e lo riporta In Progress
argument-hint: [task-id] [cosa va cambiato]
---

Task id: $1 — richiesta: "$2"

Il task deve essere in stato Verify. Registra la richiesta come commento del
reviewer (l'utente) e riapri il lavoro:
```
backlog task edit $1 --append-notes "[REVIEWER] $2"
backlog task edit $1 -s "In Progress"
```

Conferma il cambio di stato. Il prossimo passo naturale è riprendere
l'implementazione con /implement-task: la nota [AGENT] che produrrà a fine
esecuzione seguirà questa [REVIEWER] nel log del task, come una risposta.
