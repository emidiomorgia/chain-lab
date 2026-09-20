# Architecture

## Struttura del repository: monorepo frontend/backend

Il repository è un monorepo con due cartelle di primo livello, `frontend/` e `backend/`, ciascuna con un progetto autonomo (build tool, test, dipendenze proprie). Nessun modulo condiviso tra i due progetti.

## Layered architecture per il backend

Il backend è organizzato in tre livelli: `controller` (endpoint REST, JAX-RS) → `service` (logica applicativa, bean CDI `@ApplicationScoped`) → `repository` (accesso ai dati). Ogni classe dipende solo dal livello immediatamente sottostante, mai viceversa.

## Policy di test per il backend

- Almeno un unit test per ogni metodo pubblico di ogni classe, mockando le dipendenze (JUnit 5 + Mockito, senza avviare il contesto Quarkus).
- Almeno un e2e test per ogni caso d'uso esposto (JUnit 5 + `@QuarkusTest` + RestAssured), che verifica il comportamento end-to-end attraverso l'endpoint REST reale.
- Unit test ed e2e test vivono in package/cartelle separate, per mantenere netta la distinzione tra test isolati per layer e test sul caso d'uso completo.

(Decisione di riferimento: `decision-1`, task TASK-3)

## Accesso a MongoDB: Panache

L'accesso a MongoDB nel backend usa `quarkus-mongodb-panache` (pattern repository: classi che implementano `PanacheMongoRepository<Entity>`), non il `MongoClient` plain. Le entità Panache sono classi con campi pubblici mutabili (non record), coerenti con la convenzione Panache di accesso via campo.

## Database MongoDB separati per ambiente

Stessa istanza MongoDB locale (docker-compose, porta 27017), ma database logico diverso per ambiente: `chain-lab` per sviluppo/produzione, `chain-lab-test` per i test (`%test.quarkus.mongodb.database`). Nessun Testcontainers: i test che richiedono Mongo reale girano contro l'istanza locale sul db di test.

## Eccezione alla policy di test per le repository Panache

Le repository che estendono `PanacheMongoRepository` dipendono dal contesto Quarkus e da una connessione Mongo reale: i metodi ereditati non sono mockabili/isolabili con solo JUnit+Mockito. Per queste classi la policy generale "unit test senza avviare il contesto Quarkus" non si applica: i relativi test usano `@QuarkusTest` contro il database di test, pur restando in package separati dagli e2e. Service e controller restano invece testabili con JUnit+Mockito puro, iniettando la repository come mock.

(Decisione di riferimento: task TASK-4)
