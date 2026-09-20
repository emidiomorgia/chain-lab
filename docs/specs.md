# Specs

## Ambiente di sviluppo locale — MongoDB

Un file `docker-compose.yml` nella root del progetto avvia una istanza locale di MongoDB (immagine mongo:7) con database `chain-lab`, esposta sulla porta 27017 dell'host, senza autenticazione, con dati persistiti su volume Docker named. Avvio con `docker compose up`.
