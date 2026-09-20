---
id: TASK-3
title: 'Backend setup: scheletro Quarkus + ADR architettura repository'
status: Done
assignee: []
created_date: '2026-09-20 20:32'
updated_date: '2026-09-20 21:01'
labels: []
milestone: m-1
dependencies: []
ordinal: 2000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Creare la cartella backend/ nella root del repository e inizializzare al suo interno un progetto Quarkus (Java 25, build Maven) con uno scheletro minimo di endpoint REST: un health-check (es. GET /health o /ping) che ritorna 200 OK, organizzato secondo layered architecture (controller -> service -> repository), senza logica di dominio reale. Creare inoltre un ADR generale (backlog decision) che documenta: (1) la struttura del repository a due cartelle di primo livello, frontend/ e backend/, per separare i due progetti nel monorepo; (2) per il backend, l'adozione della layered architecture controller-service-repository come pattern standard; (3) la regola di test: almeno un unit test per ogni metodo pubblico di ogni classe, mockando le dipendenze (JUnit 5 + Mockito), e almeno un e2e test per ogni caso d'uso (RestAssured + @QuarkusTest).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 La cartella backend/ esiste nella root e contiene un progetto Quarkus Maven funzionante (mvn quarkus:dev si avvia senza errori) con Java 25 come target/source
- [x] #2 Il progetto backend espone un endpoint REST di health-check che risponde 200 OK, implementato seguendo la struttura a package controller/service/repository (anche se lo strato repository e' uno stub, la struttura dei package deve essere presente)
- [x] #3 Esiste una backlog decision (ADR) che documenta: struttura monorepo a due cartelle frontend/backend, layered architecture controller-service-repository per il backend, e la policy di test (unit test per ogni metodo pubblico con mock delle dipendenze; e2e test per ogni caso d'uso)
- [x] #4 L'endpoint di health-check ha almeno un unit test (con eventuali dipendenze mockate) e almeno un e2e test (@QuarkusTest + RestAssured) che verificano la risposta 200 OK
<!-- AC:END -->

## Definition of Done
<!-- DOD:BEGIN -->
- [x] #1 mvn test (o wrapper equivalente) eseguito dentro backend/ passa senza errori, includendo sia gli unit test che gli e2e test
- [x] #2 mvn quarkus:dev avvia l'applicazione e una chiamata manuale (es. curl) all'endpoint di health-check ritorna 200 OK
- [x] #3 La backlog decision (ADR) e' stata creata e referenziata dal task
<!-- DOD:END -->

## Implementation Plan

<!-- SECTION:PLAN:BEGIN -->
1. Generare lo scheletro Quarkus via API code.quarkus.io (POST /d) con groupId=com.chainlab, artifactId=backend, javaVersion=25, buildTool=MAVEN, piattaforma Quarkus 3.39.x, estensione io.quarkus:quarkus-rest; estrarre lo zip risultante in backend/ nella root del repo (mvnw/mvnw.cmd, pom.xml, application.properties inclusi).
2. Rimuovere i file demo generati di default (GreetingResource/GreetingResourceTest/GreetingResourceIT e relative risorse), mantenendo lo scheletro di progetto (pom.xml, wrapper, .gitignore locale, application.properties).
3. In pom.xml: verificare/impostare la property di compilazione Java a 25 (maven.compiler.release) e aggiungere dipendenze scope test org.mockito:mockito-core e org.mockito:mockito-junit-jupiter (in aggiunta a quarkus-junit5 e rest-assured già incluse di default dal generatore).
4. Creare struttura a package layered sotto com.chainlab.backend:
   - controller/HealthController.java: risorsa JAX-RS @Path("/health"), metodo GET che delega a HealthService
   - service/HealthService.java: bean CDI @ApplicationScoped con metodo pubblico status() che usa HealthRepository iniettato e ritorna un valore/DTO "OK"
   - repository/HealthRepository.java: bean CDI stub @ApplicationScoped con metodo pubblico (es. ping()) che rappresenta lo strato di persistenza, senza integrazione DB reale in questo task
5. Unit test (JUnit 5 + Mockito, no @QuarkusTest): src/test/java/.../service/HealthServiceTest.java — mocka HealthRepository, verifica che HealthService.status() interagisca col mock e ritorni il valore atteso.
6. E2E test (@QuarkusTest + RestAssured): src/test/java/.../HealthControllerIT.java — chiama GET /health, verifica status 200 e body atteso.
7. Creare la backlog decision (ADR): `backlog decision create "Architettura repository: monorepo frontend/backend e layered backend"` con i 3 contenuti del task (split frontend/backend, layered architecture controller-service-repository, policy di test unit+e2e); referenziare l'id della decision nelle note del task.
8. Verifica: eseguire `./mvnw test` (o mvnw.cmd) dentro backend/ per confermare che unit+e2e test passino; avviare `./mvnw quarkus:dev` e controllare manualmente con curl che GET /health risponda 200, poi arrestare il processo dev.
9. Popolare note/final-summary del task, spuntare AC/DoD, poi passaggio automatico a Verify (fuori da questo checkpoint).
<!-- SECTION:PLAN:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Implementato: generata la cartella backend/ tramite l'API code.quarkus.io (groupId com.chainlab, artifactId backend, Java 25, Maven, estensione quarkus-rest); rimossi i file demo (GreetingResource e relativi test). Aggiunte a pom.xml le dipendenze test org.mockito:mockito-core e org.mockito:mockito-junit-jupiter (versioni gestite dal quarkus-bom 3.39.4, nessuna versione esplicita necessaria).

Creata la struttura layered sotto com.chainlab.backend:
- repository/HealthRepository (bean CDI stub, metodo ping())
- service/HealthService (bean CDI, metodo status(), dipende da HealthRepository via @Inject)
- controller/HealthController (risorsa JAX-RS GET /health, delega a HealthService)

Test:
- HealthServiceTest (JUnit5 + MockitoExtension, no @QuarkusTest): mocka HealthRepository, verifica status() sia in caso OK che DOWN
- HealthControllerTest (@QuarkusTest + RestAssured): GET /health -> 200, body "OK"

Verifica eseguita: `./mvnw test` dentro backend/ passa (1 test e2e + 2 unit test, 0 failure/errori). `./mvnw quarkus:dev` avviato manualmente, curl su http://localhost:8080/health ha risposto 200 con body "OK"; processo fermato dopo la verifica.

Creata la backlog decision decision-1 "Architettura repository: monorepo frontend/backend e layered backend" (status accepted), popolata con Context/Decision/Consequences che coprono: split monorepo frontend/backend, layered architecture controller-service-repository, policy di test unit+e2e.

[REVIEW FEEDBACK] Non è stata rispettata la regola per cui ogni metodo pubblico delle classi deve avere almeno una unit test dove testare in isolamento dagli altre dipendenze. Quindi il controller deve essere testato mockando il service e il service deve essere testato mockando il repository. Invece il test e2e deve testare l'endpoint mockando il db se occorre. Voglio una separazione dalle unit test e le e2e test

[AGENT] Recepito il feedback di review: separati unit test ed e2e test per layer, coerentemente con la policy definita nell'ADR (decision-1).

- controller/HealthControllerTest.java: ora è un unit test puro (MockitoExtension, no @QuarkusTest) che mocka HealthService via @Mock/@InjectMocks e verifica che HealthController.health() deleghi al service e ne ritorni il risultato.
- service/HealthServiceTest.java: invariato, unit test puro che mocka HealthRepository (già conforme).
- repository/HealthRepositoryTest.java (nuovo): unit test diretto su HealthRepository.ping() (nessuna dipendenza da mockare, essendo lo strato più basso).
- e2e/HealthEndpointE2ETest.java (spostato da controller/HealthControllerTest.java, rinominato): unico test e2e rimasto, @QuarkusTest + RestAssured, verifica il caso d'uso "controllo stato di salute" attraverso l'endpoint REST reale end-to-end. Nessuna integrazione DB reale presente in questo skeleton, quindi nessun mock di persistenza necessario a questo livello.

Verifica: `./mvnw test` in backend/ passa con 5 test (4 unit isolati per layer + 1 e2e), 0 failure/errori.
<!-- SECTION:NOTES:END -->

## Final Summary

<!-- SECTION:FINAL_SUMMARY:BEGIN -->
Creato lo scheletro del progetto backend Quarkus (Java 25, Maven) in backend/, con layered architecture controller-service-repository e endpoint di health-check GET /health. Ogni layer ha un unit test isolato che mocka la dipendenza sottostante (controller mocka service, service mocka repository), separato dall'unico e2e test che esercita il caso d'uso attraverso l'endpoint REST reale. Aggiunta la backlog decision decision-1 che fissa la struttura monorepo frontend/backend e la policy di test.
<!-- SECTION:FINAL_SUMMARY:END -->
