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

## Configurazione status Backlog.md

La board usa quattro colonne, non tre: `To Do → In Progress → Verify → Done`.
In `backlog/config.yml`:
```yaml
statuses: ["To Do", "In Progress", "Verify", "Done"]
```
`Verify` è lo stato "implementazione fatta, in attesa di revisione (umana e/o
automatica) prima dell'approvazione finale". Non esiste un comando CLI per
modificare la lista status: va editato questo file a mano.

## Avvio di una change

1. `backlog milestone add "<nome-change>"` — crea il contenitore
2. Crea `changes/<slug-change>/` con `adr.md`, `spec.md`, `changelog.md` vuoti
   (solo intestazione, verranno popolati dal sync)

Non aprire una change per modifiche banali (typo, rename, fix di una riga):
lavora direttamente, senza ceremony.

## Ciclo per singolo task — due checkpoint di conferma, una notifica automatica

Per i checkpoint 1 e 2 non passare al passo successivo senza l'ok esplicito
dell'utente. Il passaggio a Verify (dopo l'esecuzione) è invece automatico:
non è un gate da confermare, è l'ingresso nella fase di review.

**1. Intervista → creazione task**
Prima di fare qualsiasi domanda, leggi `docs/architecture.md` e `docs/specs.md`:
sono i vincoli e le decisioni già prese, da rispettare per default. Se la
richiesta dell'utente confligge con qualcosa già scritto lì, segnalalo
esplicitamente prima di procedere — non ignorarlo e non decidere da solo se
prevale il vecchio o il nuovo.

Poi fai domande per chiarire scope, comportamento atteso, edge case. Poi:
```
backlog task create "<titolo>" -d "<descrizione>" --ac "<criterio1>" --ac "<criterio2>" --dod "<verifica1>"
```
Assegna il task al milestone della change corrente.

> **CHECKPOINT 1** — mostra descrizione, acceptance criteria e DoD. Aspetta
> conferma o richiesta di modifica prima di procedere.

**2. Piano**
Rileggi `docs/architecture.md` (pattern, stack, vincoli) prima di scrivere il
piano — il piano deve essere coerente con quanto già stabilito lì, non solo
con l'AC del task. Poi:
```
backlog task edit <id> --plan "<passi implementativi, basati sullo stato attuale del codice>"
```

> **CHECKPOINT 2** — mostra il piano. Aspetta conferma. Se l'utente vuole
> eseguire lui stesso il task, fermati qui: il piano resta come guida.

**3. Esecuzione**
Prima di scrivere codice, segna l'avvio della lavorazione:
```
backlog task edit <id> -s "In Progress"
```
Se esegue l'agente: implementa seguendo il piano. In entrambi i casi (agente o
utente), a fine implementazione popola:
```
backlog task edit <id> --append-notes "[AGENT] <cosa è stato effettivamente fatto>"
backlog task edit <id> --final-summary "<voce di changelog per questo task>"
backlog task edit <id> --check-ac <n>    # per ogni AC soddisfatto
backlog task edit <id> --check-dod <n>   # per ogni DoD verificato
```

> **CHECKPOINT 3** — mostra diff/codice, note di implementazione, AC e DoD
> spuntati. Poi, SENZA aspettare conferma, esegui subito il passo 4: la
> Verify è la fase di review, non un traguardo da confermare prima.

**4. Invio automatico in Verify**
Appena finita l'esecuzione (checkpoint 3 mostrato), esegui subito:
```
backlog task edit <id> -s Verify
```
e avvisa esplicitamente l'utente, ad esempio:
> ✅ Task <id> completato, spostato in **Verify**. Rivedi tu stesso, oppure
> chiedimi `/review-task <id>` per una code review automatica. Poi
> `/request-changes` per rimandarlo indietro o `/complete-task` per approvarlo.

Nessuna attesa di conferma qui: la Verify stessa è il momento in cui
l'utente decide, non prima.

**5. Code review automatica (su richiesta esplicita, `/review-task`)**
Su `/review-task`, rivedi il codice del task rispetto a piano, AC e DoD;
posta l'esito come nota:
```
backlog task edit <id> --append-notes "[AI REVIEW] <esito, problemi trovati, suggerimenti>"
```
Non cambiare status qui: la decisione (approvare o richiedere modifiche)
spetta all'utente dopo aver letto la nota.

**6. Decisione dell'utente**
Due esiti possibili, entrambi comandati esplicitamente dall'utente:
- **Richiesta di modifiche** — appendi la richiesta come commento del
  reviewer (l'utente) e riporta il task a In Progress:
  ```
  backlog task edit <id> --append-notes "[REVIEWER] <cosa va cambiato>"
  backlog task edit <id> -s "In Progress"
  ```
  Si riparte dal passo 3 (Esecuzione): quando l'esecuzione successiva si
  conclude, la nota `[AGENT]` del passo 3 finisce sotto quella `[REVIEWER]`,
  come una risposta — le note diventano così un thread leggibile in ordine
  cronologico, non solo un log di cosa è stato fatto.
- **Approvazione** — `backlog task edit <id> -s Done`, ESEGUITA SOLO da
  `/complete-task`, che fa anche la sync (vedi sotto). Non spostare mai un
  task da Verify a Done fuori da questo comando.

## Sync all'approvazione (Verify → Done)

**Locale (`changes/<slug>/`) — sempre:**
- `spec.md`: append di descrizione + AC del task appena approvato (Done)
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

**Popolamento diretto (bootstrap o correzioni fuori ciclo)**
Oltre alla sync legata al completamento di un task, l'utente può chiederti
in qualsiasi momento di scrivere direttamente in `docs/architecture.md` o
`docs/specs.md` — tipicamente per fissare vincoli/decisioni prima ancora di
iniziare il primo task, o per correggere/aggiungere qualcosa che non è
emerso da un task. In questo caso scrivi subito, senza creare un task e
senza passare dal ciclo a checkpoint: è un'istruzione diretta sulla
documentazione, non lavoro di implementazione.

## Chiusura di una change

Quando tutti i task del milestone sono in stato Done (non prima):
1. Verifica coerenza tra `changes/<slug>/spec.md` e stato reale del codice
2. Archivia fisicamente ogni task Done del milestone: per ciascuno,
   `backlog task archive <id>` — questo è il momento in cui i task lasciano
   la board ed entrano in `backlog/completed/` (nome interno di Backlog.md
   per i task archiviati, non è un bug se lo trovi lì e non in una cartella
   "archive")
3. `backlog milestone archive <milestone-id>`
4. Sposta `changes/<slug>/` in `changes/archive/YYYY-MM-DD-<slug>/`

Nota: l'archiviazione dei task (passo 2) è un'operazione di sola pulizia
interna a Backlog.md — la documentazione (spec/adr/changelog locali e
generali) è già stata scritta al momento del completamento di ogni task,
non qui. Se un task del milestone non è ancora Done, fermati e dimmelo
invece di proseguire con la chiusura.

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
- Non saltare i checkpoint 1 e 2 anche se il task sembra banale — è
  l'utente a decidere quando abbreviare, non l'agente di sua iniziativa. Il
  passaggio automatico a Verify (punto 4 del ciclo) non è un checkpoint e
  non richiede conferma: è per design l'unico passo che avviene da solo
- Tono nei commit e nelle note: diretto, senza filler
