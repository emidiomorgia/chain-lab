---
id: decision-1
title: 'Architettura repository: monorepo frontend/backend e layered backend'
date: '2026-09-20 20:43'
status: accepted
---
## Context

Il progetto ChainLab è un monorepo che ospita sia il frontend che il backend.
Serve fissare, prima di iniziare l'implementazione del backend, come è
organizzato il repository a livello di cartelle e quale pattern architetturale
adottare all'interno del backend, così da avere una base coerente su cui
costruire i task successivi (controller, service, repository, test).

## Decision

1. **Struttura repository**: due cartelle di primo livello, `frontend/` e
   `backend/`, ciascuna con il proprio progetto autonomo (build tool, test,
   dipendenze). Nessun modulo condiviso tra le due per ora.
2. **Architettura backend**: layered architecture a tre livelli —
   `controller` (endpoint REST, JAX-RS) → `service` (logica applicativa,
   bean CDI `@ApplicationScoped`) → `repository` (accesso ai dati). Ogni
   classe dipende solo dal livello immediatamente sottostante, mai
   viceversa.
3. **Policy di test**:
   - almeno un unit test per ogni metodo pubblico di ogni classe, mockando
     le dipendenze (JUnit 5 + Mockito, senza avviare il contesto Quarkus);
   - almeno un e2e test per ogni caso d'uso esposto (JUnit 5 + `@QuarkusTest`
     + RestAssured, che verifica il comportamento end-to-end attraverso
     l'endpoint REST reale).

## Consequences

- Frontend e backend evolvono e si versionano in modo indipendente
  all'interno dello stesso repository.
- Ogni nuova funzionalità del backend richiede minimo due classi di test
  (unit sul service, e2e sul controller), aumentando la superficie di test
  ma garantendo copertura sia a livello di unità che di comportamento
  osservabile dall'esterno.
- Lo strato `repository` è disaccoppiato dal resto tramite injection CDI,
  facilitando il mocking negli unit test.

