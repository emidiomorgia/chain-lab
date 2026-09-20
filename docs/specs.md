# Specs

## Ambiente di sviluppo locale — MongoDB

Un file `docker-compose.yml` nella root del progetto avvia una istanza locale di MongoDB (immagine mongo:7) con database `chain-lab`, esposta sulla porta 27017 dell'host, senza autenticazione, con dati persistiti su volume Docker named. Avvio con `docker compose up`.

## Backend — endpoint di health-check

Il backend (Quarkus, Java 25, in `backend/`) espone `GET /health`, che ritorna `200 OK` con body `OK`. Nessuna logica di dominio: verifica solo che l'applicazione sia in esecuzione.

## Backend — upload file e blocchi della blockchain

`POST /blocks` riceve un file via multipart/form-data (campo `file`) e crea un nuovo blocco della blockchain, persistito in MongoDB (collection `blocks`, database `chain-lab`). Se la collection è vuota, viene creato automaticamente e lazily il blocco genesi (index 0, payload nullo, previousHash convenzionale a 64 zeri). Per ogni blocco vengono calcolati: hash SHA-256 del contenuto del file (payload.hash) e hash del blocco corrente come SHA-256(timestamp + index + payload.hash + previousHash). Risposta: `201 Created` con body `{"index": n}`. Se il multipart non contiene un file o il file ha dimensione 0: `400 Bad Request`, nessun blocco creato.
