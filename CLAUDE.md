# CLAUDE.md

Istruzioni di progetto per Claude Code. Caricato automaticamente a ogni sessione:
non ripetere queste regole in chat, valgono sempre.

## Contesto

Progetto: ChainLab — imitazione di una blockchain (Quarkus + MongoDB, approccio
spec-driven), per gioco/training. Gestione task e decisioni tramite Backlog.md
(`backlog` CLI). Nessun vincolo di produzione: preferire chiarezza e velocità di
iterazione a robustezza enterprise, salvo dove esplicitamente notato.

## Due livelli di documentazione

```
backlog/                    # ledger operativo — gestito SOLO da Backlog.md CLI
├── tasks/                  # dettaglio grezzo: AC, DoD, piano, note, final summary
├── docs/                   # backlog doc — materiale di riferimento tecnico
└── decisions/              # backlog decision — ADR grezzi

docs/                        # verità distillata — SEMPRE IN APPEND
├── architecture.md          # solo decisioni architetturali durature
├── specs.md                  # solo capacità stabili del sistema
└── changelog.md               # log di TUTTO, anche le voci minori

changes/
├── <slug-change>/            # = 1 milestone Backlog.md
│   ├── adr.md                 # ADR di questa change (da backlog decision)
│   ├── spec.md                  # cosa fa/cambia questa change (da task description+AC)
│   └── changelog.md              # cosa è stato fatto in questa change (da final-summary)
└── archive/
    └── YYYY-MM-DD-<slug>/       # change chiuse, storico immutabile
```

`backlog/` è il dato grezzo e granulare, task per task. `changes/` e `docs/` sono
la narrazione leggibile che ne distilli — non aprire mai `backlog/tasks/*.md` a
mano per capire lo stato di una change: leggi `changes/<slug>/`.

## Avvio di una change

1. `backlog milestone add "<nome-change>"` — crea il contenitore
2. Crea `changes/<slug-change>/` con `adr.md`, `spec.md`, `changelog.md` vuoti
   (solo intestazione, verranno popolati dal sync)

Non aprire una change per modifiche banali (typo, rename, fix di una riga):
lavora direttamente, senza ceremony.

## Ciclo per singolo task — tre checkpoint obbligatori

Non passare al passo successivo senza l'ok esplicito dell'utente al checkpoint.

**1. Intervista → creazione task**
Fai domande per chiarire scope, comportamento atteso, edge case. Poi:
```
backlog task create "<titolo>" -d "<descrizione>" --ac "<criterio1>" --ac "<criterio2>" --dod "<verifica1>"
```
Assegna il task al milestone della change corrente.

> **CHECKPOINT 1** — mostra descrizione, acceptance criteria e DoD. Aspetta
> conferma o richiesta di modifica prima di procedere.

**2. Piano**
```
backlog task edit <id> --plan "<passi implementativi, basati sullo stato attuale del codice>"
```

> **CHECKPOINT 2** — mostra il piano. Aspetta conferma. Se l'utente vuole
> eseguire lui stesso il task, fermati qui: il piano resta come guida.

**3. Esecuzione**
Se esegue l'agente: implementa seguendo il piano. In entrambi i casi (agente o
utente), a fine implementazione popola:
```
backlog task edit <id> --append-notes "<cosa è stato effettivamente fatto>"
backlog task edit <id> --final-summary "<voce di changelog per questo task>"
backlog task edit <id> --check-ac <n>    # per ogni AC soddisfatto
backlog task edit <id> --check-dod <n>   # per ogni DoD verificato
```

> **CHECKPOINT 3** — mostra diff/codice, note di implementazione, AC e DoD
> spuntati. Aspetta conferma prima di archiviare.

**4. Archiviazione**
```
backlog task archive <id>
```
Solo dopo checkpoint 3 positivo e AC/DoD tutti spuntati.

## Sync dopo ogni archiviazione

**Locale (`changes/<slug>/`) — sempre:**
- `spec.md`: append di descrizione + AC del task appena archiviato
- `changelog.md`: append del final-summary del task
- `adr.md`: se il task referenzia una `backlog decision`, integra il contenuto
  della decisione (non duplicarla parola per parola, riassumi in ottica ADR)

**Generale (`docs/`) — con giudizio, non meccanico:**
- `docs/changelog.md`: append **sempre**, anche per task minori — una riga,
  formato `YYYY-MM-DD [<slug-change>] <final-summary sintetico>`
- `docs/specs.md`: append **solo se** il task introduce o modifica una capacità
  stabile del sistema (non per refactor interni, fix, task di supporto)
- `docs/architecture.md`: append **solo se** il task riflette una decisione
  architetturale duratura, pescando dalla `backlog decision` collegata

Se hai dubbi se una modifica è "duratura" abbastanza per `docs/specs.md` o
`docs/architecture.md`, chiedi all'utente invece di decidere da solo.

## Chiusura di una change

Quando tutti i task del milestone sono archiviati:
1. Verifica coerenza tra `changes/<slug>/spec.md` e stato reale del codice
2. `backlog milestone archive <milestone-id>`
3. Sposta `changes/<slug>/` in `changes/archive/YYYY-MM-DD-<slug>/`

## Branching

- Grana per-task, non per-change: un branch per task (`task/<id>-<slug-breve>`),
  coerente con la raccomandazione stessa di Backlog.md ("one task = one PR") e
  con l'enfasi su esecuzioni chirurgiche verificabili singolarmente
- Se preferisci raggruppare più task correlati in un solo branch/PR per la
  change, dillo esplicitamente prima di iniziare: non è il default
- Merge in main solo dopo checkpoint 3 positivo sul task; niente merge con
  AC/DoD non spuntati

## Regole generali

- Non modificare `backlog/tasks/*.md` a mano: sempre via CLI `backlog`, mai
  editing diretto (rischio di disallineare ID e metadati)
- Non saltare checkpoint anche se il task sembra banale — è l'utente a
  decidere quando abbreviare, non l'agente di sua iniziativa
- Tono nei commit e nelle note: diretto, senza filler

<!-- BACKLOG.MD GUIDELINES START -->
<!-- backlog.md-instructions-version: 1.52.0 -->
<CRITICAL_INSTRUCTION>

## Backlog.md Workflow

This project uses Backlog.md for task and project management.

**At the beginning of each conversation in this project, run `backlog instructions overview` before answering or taking action. Re-read it only if you have not read it yet in the current conversation.**

Use the overview to decide whether to search, read, create, or update Backlog tasks.

Before task lifecycle actions, read the matching detailed guide:
- `backlog instructions task-creation` before creating or splitting tasks
- `backlog instructions task-execution` before planning, changing status or assignee, adding a plan or implementation notes, or implementing task work
- `backlog instructions task-finalization` before checking acceptance criteria, writing final summaries, or moving tasks to terminal statuses

Use `backlog <command> --help` before running unfamiliar commands. Help shows options, fields, and examples.

Do not edit Backlog task, draft, document, decision, or milestone markdown files directly. Use the `backlog` CLI so metadata, relationships, and history stay consistent.

</CRITICAL_INSTRUCTION>
<!-- BACKLOG.MD GUIDELINES END -->
