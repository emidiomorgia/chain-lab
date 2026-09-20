---
id: TASK-1
title: Create README.md
status: Done
assignee:
  - '@emidio.morgia'
created_date: '2026-09-20 19:37'
updated_date: '2026-09-20 19:44'
labels: []
milestone: m-0
dependencies: []
type: feature
ordinal: 1000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Il progetto ChainLab non ha ancora un README che ne spieghi lo scopo e la struttura. Serve un documento in inglese, stile GitHub, che descriva il progetto (demo blockchain con Quarkus + MongoDB + React/TypeScript per l'upload e la verifica di autenticità di file), il concetto di dominio (payload = nome file, dimensione, hash; blocco mongo = progressivo, timestamp, payload, hash payload, hash blocco precedente, hash blocco corrente calcolato su timestamp+progressivo+hash payload+hash blocco precedente), le due operazioni esposte al frontend (inserimento file → restituzione progressivo; verifica file+progressivo → esito integrità su payload, hash payload, hash blocco precedente, hash blocco corrente, con 404 se il progressivo non esiste), lo stack tecnologico, la struttura a monorepo (cartelle backend/ e frontend/) e le istruzioni operative per l'esecuzione in locale (docker compose, avvio backend Quarkus, avvio frontend React), anche se questi comandi/file non esistono ancora nel repo e verranno introdotti da task successivi.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Il file README.md in root descrive in inglese lo scopo del progetto (demo blockchain di verifica autenticità file) e lo stack tecnologico (Quarkus, MongoDB, React, TypeScript, Docker)
- [x] #2 Il README spiega ad alto livello il modello dati del blocco (progressivo, timestamp, payload, hash payload, hash blocco precedente, hash blocco corrente) e la composizione del payload (nome file, dimensione, hash)
- [x] #3 Il README descrive ad alto livello le due funzionalità esposte al frontend: inserimento file con restituzione del progressivo, e verifica file+progressivo con relativo esito (incluso il caso 404 se il progressivo non esiste)
- [x] #4 Il README elenca la struttura del monorepo con le cartelle backend/ e frontend/
- [x] #5 Il README include istruzioni operative per l'esecuzione in locale: docker compose, avvio del backend Quarkus, avvio del frontend React
<!-- AC:END -->

## Definition of Done
<!-- DOD:BEGIN -->
- [x] #1 Il README è in formato Markdown, in inglese, con formattazione stile GitHub (titoli, sezioni, eventuali badge/code block dove utile)
<!-- DOD:END -->

## Implementation Plan

<!-- SECTION:PLAN:BEGIN -->
1. Sovrascrivere README.md in root con struttura stile GitHub in inglese:
   - Titolo + badge opzionali (license MIT) + breve tagline
   - Sezione 'Overview': descrizione del progetto come demo didattica/training di blockchain con verifica autenticità file, stack Quarkus + MongoDB + React/TypeScript + Docker
   - Sezione 'How it works' / 'Domain model': spiegazione payload (filename, size, hash) e struttura del blocco Mongo (sequence/progressivo, timestamp, payload, payloadHash, previousBlockHash, blockHash calcolato su timestamp+sequence+payloadHash+previousBlockHash)
   - Sezione 'Features': due funzionalità esposte al frontend — (a) upload file → registrazione blocco e restituzione del progressivo; (b) verifica file+progressivo → controllo integrità (payload name/size, payload hash, previous block hash, block hash) con esito e 404 se il progressivo non esiste
   - Sezione 'Tech stack': elenco Quarkus, MongoDB, React, TypeScript, Docker con breve razionale di ciascuna scelta
   - Sezione 'Project structure': monorepo con cartelle backend/ (Quarkus) e frontend/ (React+TypeScript), nota che sono placeholder per sviluppo futuro
   - Sezione 'Getting started / Running locally': prerequisiti (Docker, JDK, Node), comando docker compose up -d per MongoDB, avvio backend (./mvnw quarkus:dev o gradlew), avvio frontend (npm install && npm run dev), porte ipotetiche, nota che compose file e script verranno aggiunti in task successivi
   - Sezione 'License': riferimento a LICENSE (MIT)
2. Verificare che ogni AC sia coperto rileggendo il file prodotto
3. Nessun codice/file da creare oltre README.md: nessun test automatico applicabile, la DoD si verifica a lettura manuale del file (markdown valido, inglese, formattazione GitHub-style)
<!-- SECTION:PLAN:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Riscritto README.md in root (inglese, stile GitHub) con sezioni: Overview, Domain model (payload + block, incluso blockHash su timestamp+sequence+payloadHash+previousBlockHash), Features (register file → sequence; verify file+sequence → esito integrità su 4 livelli + 404 se sequence non trovato), Tech stack (Quarkus, MongoDB, React, TypeScript, Docker), Project structure (monorepo backend/ e frontend/ come placeholder), Getting started (docker compose per Mongo, avvio backend quarkus:dev, avvio frontend npm run dev), License. Nessun file di codice toccato oltre README.md.
<!-- SECTION:NOTES:END -->

## Final Summary

<!-- SECTION:FINAL_SUMMARY:BEGIN -->
Aggiunto README.md iniziale del progetto: descrizione del dominio blockchain (payload, blocco, hash chain), funzionalità upload/verify, stack tecnologico, struttura monorepo e istruzioni di avvio locale.
<!-- SECTION:FINAL_SUMMARY:END -->
