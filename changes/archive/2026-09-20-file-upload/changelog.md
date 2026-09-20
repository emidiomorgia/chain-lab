# Changelog — File Upload

- TASK-4: Aggiunto endpoint POST /blocks: upload file via multipart, calcolo hash SHA-256 del contenuto, creazione lazy del blocco genesi e persistenza su MongoDB (Panache) del blocco con timestamp, index, payload (filename/size/hash), previousHash e hash del blocco, con db dev/test separati sulla stessa istanza Mongo locale.
