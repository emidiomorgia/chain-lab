# ChainLab

A small training/demo project that simulates a blockchain used to certify the
authenticity of files uploaded from a web frontend.

## Overview

ChainLab is not a production blockchain — it is a spec-driven learning project
built to explore how a simple, MongoDB-backed append-only chain can be used to
prove that a file has not been tampered with since it was first registered.

A user uploads a file from the frontend. The backend computes a fingerprint of
that file, appends a new block to the chain, and returns the block's sequence
number. Later, the same (or a different) user can re-upload the file together
with that sequence number to ask ChainLab: *"is this still the exact same file
I registered as block #N?"*.

## Domain model

### Payload

Every block wraps a payload describing the file that was registered:

| Field      | Description                          |
|------------|---------------------------------------|
| `filename` | Original name of the uploaded file    |
| `size`     | File size in bytes                    |
| `hash`     | Hash of the file content               |

### Block

Each payload is wrapped into a block, persisted as a document in a MongoDB
collection:

| Field                | Description                                                                 |
|----------------------|------------------------------------------------------------------------------|
| `sequence`           | Progressive block number (also the identifier returned to the client)       |
| `timestamp`          | Time the block was created                                                   |
| `payload`            | The payload described above (`filename`, `size`, `hash`)                    |
| `payloadHash`        | Hash of the payload                                                          |
| `previousBlockHash`  | Copy of the `blockHash` of the previous block in the chain                  |
| `blockHash`          | Hash computed over `timestamp` + `sequence` + `payloadHash` + `previousBlockHash` |

The `blockHash` chains every block to the one before it, the same way a real
blockchain does: changing any past block would break the hash chain for every
block that follows it.

## Features

The frontend exposes two operations backed by the chain:

### 1. Register a file

The user uploads a file. The backend:

1. Computes the payload (`filename`, `size`, `hash`).
2. Appends a new block to the chain, linking it to the previous block's hash.
3. Persists the block in MongoDB.
4. Returns the new block's `sequence` number to the caller.

### 2. Verify a file

The user uploads a file together with a previously issued `sequence` number.
The backend looks up the corresponding block and checks:

- **Payload integrity** — the uploaded file's name and size match the
  block's stored payload.
- **Payload hash integrity** — the hash of the uploaded file matches the
  block's stored `payloadHash`.
- **Chain integrity** — the block's `previousBlockHash` matches the actual
  `blockHash` of the preceding block.
- **Block integrity** — the block's own `blockHash` matches a hash
  recomputed from its content.

The result is returned as a verification outcome. If the requested
`sequence` does not correspond to any stored block, the API responds with
**404 Not Found**.

## Tech stack

- **[Quarkus](https://quarkus.io/)** — backend framework (Java), used to
  expose the upload/verify REST API and to run the blockchain logic.
- **[MongoDB](https://www.mongodb.com/)** — storage for the chain: each block
  is a document in an append-only collection.
- **[React](https://react.dev/)** + **[TypeScript](https://www.typescriptlang.org/)**
  — frontend used to upload files and to trigger/display verification results.
- **[Docker](https://www.docker.com/)** — used to run MongoDB (and, longer
  term, the full stack) locally without manual setup.

## Project structure

This is a monorepo with two top-level components:

```
chain-lab/
├── backend/    # Quarkus application: REST API, chain/hashing logic, MongoDB access
├── frontend/   # React + TypeScript application: upload and verification UI
├── backlog/    # Backlog.md task ledger
├── changes/    # Distilled specs/ADRs/changelogs per change
└── docs/       # Distilled, durable architecture/specs/changelog
```

> `backend/` and `frontend/` are the planned locations for the application
> code; they will be populated by upcoming tasks.

## Getting started

### Prerequisites

- [Docker](https://www.docker.com/) and Docker Compose
- JDK 21+ (for the Quarkus backend)
- Node.js 20+ (for the React frontend)

### Run MongoDB

```bash
docker compose up -d
```

> A `docker-compose.yml` provisioning MongoDB will be added as part of the
> backend setup task.

### Run the backend (Quarkus)

```bash
cd backend
./mvnw quarkus:dev
```

### Run the frontend (React)

```bash
cd frontend
npm install
npm run dev
```

## License

Distributed under the terms of the [MIT License](LICENSE).
