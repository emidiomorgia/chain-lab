# Spec — File Upload

## TASK-4 — POST /blocks — upload file e persistenza blocco blockchain

Endpoint REST `POST /blocks` che riceve un file via multipart/form-data e crea un nuovo blocco della blockchain in MongoDB (collection `blocks`). Il servizio: (1) se la collection è vuota, crea lazily il blocco genesi (index 0, payload nullo, previousHash convenzionale a 64 zeri, hash calcolato normalmente); (2) legge il file dal multipart, ne determina nome e dimensione, calcola l'hash SHA-256 del contenuto; (3) determina l'index (0-indexed: genesi=0, primo upload=1, ecc.); (4) recupera l'hash del blocco precedente (ultimo blocco in ordine di index); (5) costruisce il documento con timestamp, index, payload {filename, size, hash}, previousHash, e calcola l'hash del blocco corrente come SHA-256(timestamp+index+payload.hash+previousHash); (6) salva il documento in Mongo. Rispetta la layered architecture (controller/service/repository).

Acceptance Criteria:
- POST /blocks con multipart contenente un file valido risponde 201 Created con body contenente almeno l'index del blocco creato
- Il documento salvato in Mongo contiene timestamp, index, payload (filename, size, hash SHA-256 del contenuto), previousHash, hash del blocco corrente
- Se la collection 'blocks' è vuota, prima di salvare il blocco richiesto viene creato automaticamente il blocco genesi (index 0)
- Se il multipart non contiene un file o il file ha dimensione 0, l'endpoint risponde 400 Bad Request senza creare alcun blocco
