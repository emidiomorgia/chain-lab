---
id: TASK-2
title: Docker compose for mongo
status: Done
assignee: []
created_date: '2026-09-20 20:00'
updated_date: '2026-09-20 20:11'
labels: []
milestone: m-1
dependencies: []
ordinal: 1000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Creare un file docker-compose di sviluppo nella root del progetto per avviare una istanza locale di MongoDB (immagine mongo:7) con database chain-lab, da usare durante lo sviluppo. Nessuna autenticazione, porta esposta 27017, dati persistiti su volume Docker named.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Il file docker-compose (es. docker-compose.yml) è presente nella root del progetto e definisce un servizio mongo basato su immagine mongo:7
- [x] #2 Il servizio espone la porta 27017 sull'host e crea/usa un database di nome chain-lab
- [x] #3 I dati di mongo sono persistiti tramite un volume Docker named, così da sopravvivere a stop/restart del container
- [x] #4 Nessuna autenticazione richiesta per la connessione (setup di sviluppo)
<!-- AC:END -->

## Definition of Done
<!-- DOD:BEGIN -->
- [x] #1 docker compose up avvia il container mongo senza errori e permette la connessione al database chain-lab sulla porta 27017 dall'host
<!-- DOD:END -->

## Implementation Plan

<!-- SECTION:PLAN:BEGIN -->
1. Creare docker-compose.yml nella root del progetto con un servizio 'mongo' basato su immagine mongo:7. 2. Configurare mapping porta 27017:27017. 3. Configurare variabile MONGO_INITDB_DATABASE=chain-lab per creare il db all'avvio. 4. Definire un volume Docker named (es. mongo-data) montato su /data/db per la persistenza. 5. Nessuna variabile di autenticazione (MONGO_INITDB_ROOT_*) impostata, cosi' il servizio resta senza auth. 6. Verificare con 'docker compose up' che il container parta senza errori e che sia possibile connettersi a mongodb://localhost:27017/chain-lab dall'host.
<!-- SECTION:PLAN:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Creato docker-compose.yml nella root con servizio mongo (immagine mongo:7), porta 27017:27017, MONGO_INITDB_DATABASE=chain-lab, volume named mongo-data su /data/db, nessuna auth configurata. Verificato con 'docker compose up -d': container avviato correttamente, connessione riuscita a mongodb://localhost:27017/chain-lab (ping ok via mongosh), poi fermato con 'docker compose down'.
<!-- SECTION:NOTES:END -->

## Final Summary

<!-- SECTION:FINAL_SUMMARY:BEGIN -->
Aggiunto docker-compose.yml per avviare MongoDB 7 locale (db chain-lab, porta 27017, senza auth, dati persistiti su volume named) per lo sviluppo.
<!-- SECTION:FINAL_SUMMARY:END -->
