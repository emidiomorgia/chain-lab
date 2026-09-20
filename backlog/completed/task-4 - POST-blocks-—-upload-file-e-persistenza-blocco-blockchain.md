---
id: TASK-4
title: POST /blocks — upload file e persistenza blocco blockchain
status: Done
assignee: []
created_date: '2026-09-20 21:12'
updated_date: '2026-09-20 21:46'
labels: []
milestone: m-2
dependencies: []
ordinal: 3000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Endpoint REST POST /blocks che riceve un file via multipart/form-data e crea un nuovo blocco della blockchain in MongoDB (collection 'blocks'). Il servizio: (1) se la collection è vuota, crea lazily il blocco genesi (progressivo 0, payload nullo, hashPrecedente convenzionale a zeri, hash calcolato normalmente); (2) legge il file dal multipart, ne determina nome e dimensione, calcola l'hash SHA-256 del contenuto; (3) determina il progressivo contando i blocchi esistenti + 1; (4) recupera l'hash del blocco precedente (ultimo blocco in ordine di progressivo); (5) costruisce il documento con timestamp, progressivo, payload {nome, dimensione, hash}, hashPrecedente, e calcola l'hash del blocco corrente come SHA-256(timestamp+progressivo+payload.hash+hashPrecedente); (6) salva il documento in Mongo. Rispetta la layered architecture (controller/service/repository) e le policy di test già definite in docs/architecture.md.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 POST /blocks con multipart contenente un file valido risponde 201 Created con body contenente almeno il progressivo del blocco creato
- [x] #2 Il documento salvato in Mongo contiene timestamp, progressivo, payload (nome, dimensione, hash SHA-256 del contenuto), hash del blocco precedente, hash del blocco corrente
- [x] #3 Se la collection 'blocks' è vuota, prima di salvare il blocco richiesto viene creato automaticamente il blocco genesi (progressivo 0)
- [x] #4 Se il multipart non contiene un file o il file ha dimensione 0, l'endpoint risponde 400 Bad Request senza creare alcun blocco
<!-- AC:END -->

## Definition of Done
<!-- DOD:BEGIN -->
- [x] #1 Unit test per ogni metodo pubblico di controller/service/repository coinvolti (JUnit 5 + Mockito), e almeno un e2e test (@QuarkusTest + RestAssured) che copre upload con successo, caso genesi lazy e caso file mancante/vuoto
<!-- DOD:END -->

## Implementation Plan

<!-- SECTION:PLAN:BEGIN -->
1. Dipendenze (backend/pom.xml): quarkus-mongodb-panache (Panache repository pattern, sostituisce il MongoClient plain — include comunque quarkus-mongodb-client come transitiva) + quarkus-rest-jackson (serializzazione JSON della response). Il multipart (@RestForm / FileUpload) è già supportato da quarkus-rest.

2. Config (backend/src/main/resources/application.properties), db di sviluppo separato da quello di test, stessa istanza Mongo locale (docker-compose, docs/specs.md):
   quarkus.mongodb.connection-string=mongodb://localhost:27017
   quarkus.mongodb.database=chain-lab
   %test.quarkus.mongodb.database=chain-lab-test
   Nessun Testcontainers (valutato e scartato, vedi punto 7): sia gli e2e sia i test di repository che richiedono Mongo reale girano contro l'istanza locale, sul db 'chain-lab-test'.

3. Modello dominio (nuovo package backend/src/main/java/com/chainlab/backend/model/), nomi in inglese:
   - Payload.java — record (filename: String, size: long, hash: String)
   - Block.java — classe Panache-compatible (campi pubblici mutabili, richiesto da Panache: id gestito da Mongo, non record): public ObjectId id; public Instant timestamp; public long index; public Payload payload (null per il blocco genesi); public String previousHash; public String hash;

4. Repository (backend/src/main/java/com/chainlab/backend/repository/BlockRepository.java), @ApplicationScoped, implements PanacheMongoRepository<Block>:
   - count(): ereditato da Panache
   - findLastBlock(): Optional<Block> → findAll(Sort.descending(\"index\")).firstResultOptional() (metodo custom aggiunto)
   - insert(Block): persist(block) (ereditato, wrappato per omogeneità con lo strato service se serve un nome esplicito)

5. Service (backend/src/main/java/com/chainlab/backend/service/BlockService.java), @ApplicationScoped, inietta BlockRepository:
   - createBlock(String filename, long size, Path filePath): long
     a. ensureGenesisBlock() — se blockRepository.count() == 0, crea e persiste il blocco genesi: index 0, payload null, previousHash = 64 zeri, hash calcolato con la stessa formula (su payloadHash=\"\")
     b. index = blockRepository.count() (0-indexed: genesi=0, primo upload=1, ecc.)
     c. last = blockRepository.findLastBlock().orElseThrow() (garantito presente dopo ensureGenesisBlock)
     d. payloadHash = sha256 del contenuto del file letto in streaming da filePath
     e. timestamp = Instant.now(); hash = sha256(timestamp + index + payloadHash + last.hash)
     f. costruisce e persiste il Block, ritorna index
   - metodi privati sha256(Path) e sha256(String) con java.security.MessageDigest

6. Controller (backend/src/main/java/com/chainlab/backend/controller/BlockController.java), nomi in inglese:
   - @Path(\"/blocks\"), @POST, @Consumes(MULTIPART_FORM_DATA), @Produces(APPLICATION_JSON)
   - parametro @RestForm(\"file\") FileUpload file
   - se file == null || file.size() == 0 → Response 400 con messaggio d'errore, non chiama il service
   - altrimenti chiama blockService.createBlock(file.fileName(), file.size(), file.uploadedFile()), ritorna Response 201 con body {\"index\": n} (nuovo record BlockCreatedResponse nel package controller.dto)

7. Test — valutazione riportata: Panache lega la repository al contesto Quarkus (i metodi ereditati non sono mockabili/isolabili senza un Mongo reale), quindi la policy 'unit test senza contesto Quarkus' di docs/architecture.md non è applicabile al layer repository per questo task. Decisione: niente Testcontainers dedicato — i test che necessitano di Mongo reale (repository, e2e) girano con @QuarkusTest contro il db locale 'chain-lab-test' (punto 8). Da segnalare esplicitamente come eccezione alla policy generale quando si aggiorna la documentazione a fine task.
   - repository/BlockRepositoryTest.java — @QuarkusTest, pulisce la collection prima di ogni test; verifica count(), findLastBlock() (assente su collection vuota, presente e corretto dopo insert), insert()/persist() (documento salvato con i campi attesi)
   - service/BlockServiceTest.java — puro JUnit+Mockito (BlockRepository qui resta mockabile perché iniettato come dipendenza nel service, non serve contesto Quarkus): caso collection vuota (crea genesi poi il blocco, index=1), caso collection non vuota (nessuna genesi, index = count corrente, previousHash = hash dell'ultimo blocco)
   - controller/BlockControllerTest.java — puro JUnit+Mockito, mocka BlockService: caso file valido (201 + index nel body, service invocato), caso file nullo/size 0 (400, service MAI invocato)

8. Test e2e (backend/src/test/java/com/chainlab/backend/e2e/BlockUploadE2ETest.java), @QuarkusTest + RestAssured, contro Mongo locale db 'chain-lab-test' (%test profile attivo automaticamente):
   - pulizia collection 'blocks' prima di ogni test (isolamento)
   - upload con file valido → 201, body.index == 1, verifica diretta via BlockRepository che esistano 2 documenti (genesi + blocco caricato) con i campi attesi
   - upload senza parte 'file' nel multipart → 400
   - upload con file di dimensione 0 → 400

9. Verifica finale: `cd backend && ./mvnw test` (richiede Mongo locale attivo, `docker compose up -d` dalla root se non già avviato — il db 'chain-lab-test' viene creato automaticamente da Mongo al primo utilizzo)
<!-- SECTION:PLAN:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
[AGENT] Implementato POST /blocks: dipendenze quarkus-mongodb-panache + quarkus-rest-jackson aggiunte a backend/pom.xml; application.properties con db dev 'chain-lab' e db test 'chain-lab-test' (stessa istanza Mongo locale, nessun Testcontainers). Modello: Payload (record: filename, size, hash) e Block (classe Panache-compatible: id, timestamp, index, payload, previousHash, hash). BlockRepository extends PanacheMongoRepository<Block> con findLastBlock() custom (sort desc su index). BlockService.createBlock(filename,size,filePath): crea lazy il blocco genesi (index 0, payload null, previousHash 64 zeri) se la collection è vuota, poi calcola index=count(), hash SHA-256 del file in streaming, e hash del blocco come SHA-256(timestamp+index+payloadHash+previousHash). BlockController: POST /blocks multipart, @RestForm FileUpload, valida file nullo/size 0 -> 400, altrimenti 201 con body {index}. Test: BlockServiceTest e BlockControllerTest puri JUnit+Mockito (no contesto Quarkus); BlockRepositoryTest e BlockUploadE2ETest come @QuarkusTest contro Mongo locale (db chain-lab-test) — eccezione motivata alla policy generale, dato che PanacheMongoRepository non è mockabile in isolamento. Un test e2e (upload senza parte 'file') è stato adattato durante l'esecuzione: inviare una richiesta priva di multipart causa 415 (rifiutata da JAX-RS per @Consumes prima del controller), quindi il test ora invia un multipart valido ma senza il campo 'file', così la validazione applicativa (file==null) viene davvero esercitata. Suite completa: 17/17 test verdi, verificato con Mongo locale attivo (docker compose up).

[AI REVIEW] Codice coerente con piano, AC e DoD. Verificato: genesi lazy (index 0, previousHash 64 zeri), calcolo hash payload/blocco secondo la formula pianificata, index 0-indexed coerente in service/repository/e2e, validazione file nullo/size 0 -> 400 senza toccare il repository, risposta 201 con {index}. Suite 17/17 verde, separazione db dev/test (chain-lab / chain-lab-test) confermata. Nessun problema bloccante. Osservazioni minori non bloccanti: (1) il messaggio d'errore 400 in BlockController è una stringa raw mentre l'endpoint dichiara @Produces(APPLICATION_JSON) — funziona ma non è un vero JSON strutturato; (2) BlockRepository.insert() è un wrapper 1:1 su persist() senza logica propria, leggero ma coerente col piano; (3) l'eccezione alla policy test 'niente contesto Quarkus per gli unit test' (BlockRepositoryTest usa @QuarkusTest per via di Panache) era già stata dichiarata e approvata nel piano — da ricordare in sede di sync documentazione a fine task, non è un difetto del codice.
<!-- SECTION:NOTES:END -->

## Final Summary

<!-- SECTION:FINAL_SUMMARY:BEGIN -->
Aggiunto endpoint POST /blocks: upload file via multipart, calcolo hash SHA-256 del contenuto, creazione lazy del blocco genesi e persistenza su MongoDB (Panache) del blocco con timestamp, index, payload (filename/size/hash), previousHash e hash del blocco, con db dev/test separati sulla stessa istanza Mongo locale.
<!-- SECTION:FINAL_SUMMARY:END -->
