# Changelog

2026-09-20 [enhance-readme-md] Aggiunto README.md iniziale del progetto: descrizione del dominio blockchain (payload, blocco, hash chain), funzionalità upload/verify, stack tecnologico, struttura monorepo e istruzioni di avvio locale.
2026-09-20 [project-setup] Aggiunto docker-compose.yml per MongoDB 7 locale (db chain-lab, porta 27017, senza auth, volume named) per lo sviluppo.
2026-09-20 [project-setup] Creato lo scheletro backend Quarkus (Java 25) con layered architecture controller-service-repository, endpoint di health-check GET /health e relativa ADR sulla struttura del monorepo e sulla policy di test.
2026-09-20 [file-upload] Aggiunto endpoint POST /blocks: upload file multipart, hash SHA-256 del contenuto, blocco genesi lazy e persistenza su MongoDB (Panache) del blocco della blockchain.
