# Spec — Enhance README.md

## TASK-1 — Create README.md

Il progetto ChainLab non aveva un README che ne spiegasse scopo e struttura.
Serviva un documento in inglese, stile GitHub, che descrivesse il progetto
(demo blockchain con Quarkus + MongoDB + React/TypeScript per l'upload e la
verifica di autenticità di file), il modello di dominio (payload = nome file,
dimensione, hash; blocco Mongo = progressivo, timestamp, payload, hash
payload, hash blocco precedente, hash blocco corrente calcolato su
timestamp+progressivo+hash payload+hash blocco precedente), le due
operazioni esposte al frontend (inserimento file → restituzione progressivo;
verifica file+progressivo → esito integrità su payload, hash payload, hash
blocco precedente, hash blocco corrente, con 404 se il progressivo non
esiste), lo stack tecnologico, la struttura a monorepo (cartelle backend/ e
frontend/) e le istruzioni operative per l'esecuzione in locale.

Acceptance Criteria:
- Il file README.md in root descrive in inglese lo scopo del progetto e lo
  stack tecnologico (Quarkus, MongoDB, React, TypeScript, Docker)
- Il README spiega ad alto livello il modello dati del blocco e la
  composizione del payload
- Il README descrive ad alto livello le due funzionalità esposte al
  frontend (inserimento con progressivo, verifica con esito e 404)
- Il README elenca la struttura del monorepo con le cartelle backend/ e
  frontend/
- Il README include istruzioni operative per l'esecuzione in locale (docker
  compose, avvio backend Quarkus, avvio frontend React)
