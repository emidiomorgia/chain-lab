# ADR — Project Setup

## Architettura repository: monorepo frontend/backend e layered backend

**Contesto:** ChainLab è un monorepo che ospita sia frontend che backend; serve fissare la struttura delle cartelle e il pattern architetturale del backend prima di iniziare l'implementazione.

**Decisione:**
1. Struttura repository a due cartelle di primo livello, `frontend/` e `backend/`, ciascuna con progetto autonomo (build, test, dipendenze indipendenti).
2. Backend organizzato secondo layered architecture a tre livelli: `controller` (JAX-RS) → `service` (bean CDI `@ApplicationScoped`) → `repository` (accesso ai dati). Dipendenza solo verso il livello sottostante.
3. Policy di test: almeno un unit test per ogni metodo pubblico di ogni classe, mockando le dipendenze (JUnit 5 + Mockito, senza contesto Quarkus); almeno un e2e test per ogni caso d'uso esposto (JUnit 5 + `@QuarkusTest` + RestAssured, sull'endpoint REST reale).

**Conseguenze:** frontend e backend evolvono in modo indipendente nello stesso repository; ogni nuova funzionalità del backend richiede sia test unitari isolati per layer sia un test e2e sul caso d'uso; lo strato `repository` è disaccoppiato via injection CDI per facilitare il mocking.

(Decisione di riferimento: `decision-1`)
