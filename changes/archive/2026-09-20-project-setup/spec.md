# Spec — Project Setup

## Docker compose for mongo

Creare un file docker-compose di sviluppo nella root del progetto per avviare una istanza locale di MongoDB (immagine mongo:7) con database chain-lab, da usare durante lo sviluppo. Nessuna autenticazione, porta esposta 27017, dati persistiti su volume Docker named.

**Acceptance criteria:**
- Il file docker-compose (es. docker-compose.yml) è presente nella root del progetto e definisce un servizio mongo basato su immagine mongo:7
- Il servizio espone la porta 27017 sull'host e crea/usa un database di nome chain-lab
- I dati di mongo sono persistiti tramite un volume Docker named, così da sopravvivere a stop/restart del container
- Nessuna autenticazione richiesta per la connessione (setup di sviluppo)

## Backend setup: scheletro Quarkus + ADR architettura repository

Creare la cartella backend/ nella root del repository e inizializzare al suo interno un progetto Quarkus (Java 25, build Maven) con uno scheletro minimo di endpoint REST: un health-check (es. GET /health o /ping) che ritorna 200 OK, organizzato secondo layered architecture (controller -> service -> repository), senza logica di dominio reale. Creare inoltre un ADR generale (backlog decision) che documenta: (1) la struttura del repository a due cartelle di primo livello, frontend/ e backend/, per separare i due progetti nel monorepo; (2) per il backend, l'adozione della layered architecture controller-service-repository come pattern standard; (3) la regola di test: almeno un unit test per ogni metodo pubblico di ogni classe, mockando le dipendenze (JUnit 5 + Mockito), e almeno un e2e test per ogni caso d'uso (RestAssured + @QuarkusTest).

**Acceptance criteria:**
- La cartella backend/ esiste nella root e contiene un progetto Quarkus Maven funzionante (mvn quarkus:dev si avvia senza errori) con Java 25 come target/source
- Il progetto backend espone un endpoint REST di health-check che risponde 200 OK, implementato seguendo la struttura a package controller/service/repository (anche se lo strato repository è uno stub, la struttura dei package deve essere presente)
- Esiste una backlog decision (ADR) che documenta: struttura monorepo a due cartelle frontend/backend, layered architecture controller-service-repository per il backend, e la policy di test (unit test per ogni metodo pubblico con mock delle dipendenze; e2e test per ogni caso d'uso)
- L'endpoint di health-check ha almeno un unit test (con eventuali dipendenze mockate) e almeno un e2e test (@QuarkusTest + RestAssured) che verificano la risposta 200 OK
