---
name: verify
description: Build/launch/drive recipe for verifying the ChainLab backend (Quarkus) at its HTTP surface.
---

# Verifying the backend

Surface: HTTP, served by Quarkus dev mode on `http://localhost:8080`.

## Launch

From `backend/`:

```bash
./mvnw -q -B quarkus:dev > /tmp/verify-dev.log 2>&1 &
```

Startup takes a few seconds. Poll until ready instead of a fixed sleep:

```bash
for i in $(seq 1 20); do
  code=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/health)
  [ "$code" = "200" ] && break
  sleep 2
done
```

## Drive

```bash
curl -s -i http://localhost:8080/health   # -> 200 OK, body "OK"
```

## Gotchas

- On Windows/Git Bash, `mvnw` runs fine but there is no clean way to kill
  just the dev-mode JVM by job id from this shell — `taskkill //F //IM
  java.exe` works but kills *all* java.exe processes on the box. Acceptable
  in this dev-only environment, but be aware before using it elsewhere.
- Quarkus's default 404 page lists the live resource endpoints — useful for
  a quick sanity check of what's actually registered without reading code.
- `mvn`/`java` are not needed on PATH; the project's `mvnw`/`mvnw.cmd`
  wrapper downloads its own Maven distribution on first run (needs network
  access to `repo.maven.apache.org` and Maven Central).
