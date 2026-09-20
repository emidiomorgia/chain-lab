# Spec — Project Setup

## Docker compose for mongo

Creare un file docker-compose di sviluppo nella root del progetto per avviare una istanza locale di MongoDB (immagine mongo:7) con database chain-lab, da usare durante lo sviluppo. Nessuna autenticazione, porta esposta 27017, dati persistiti su volume Docker named.

**Acceptance criteria:**
- Il file docker-compose (es. docker-compose.yml) è presente nella root del progetto e definisce un servizio mongo basato su immagine mongo:7
- Il servizio espone la porta 27017 sull'host e crea/usa un database di nome chain-lab
- I dati di mongo sono persistiti tramite un volume Docker named, così da sopravvivere a stop/restart del container
- Nessuna autenticazione richiesta per la connessione (setup di sviluppo)
