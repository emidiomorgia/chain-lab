# Changelog — Project Setup

- Aggiunto docker-compose.yml per avviare MongoDB 7 locale (db chain-lab, porta 27017, senza auth, dati persistiti su volume named) per lo sviluppo.
- Creato lo scheletro del progetto backend Quarkus (Java 25, Maven) in backend/, con layered architecture controller-service-repository e endpoint di health-check GET /health. Ogni layer ha un unit test isolato che mocka la dipendenza sottostante (controller mocka service, service mocka repository), separato dall'unico e2e test che esercita il caso d'uso attraverso l'endpoint REST reale. Aggiunta la backlog decision decision-1 che fissa la struttura monorepo frontend/backend e la policy di test.
