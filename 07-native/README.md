# 07 — Native: The Wanneeikel Free Press

![Wanneeikel Free Press](wanneeikel-free-press.png)

## Wanneeikel, Western Germany, Spring 1972

Behind the supermarket, at the end of a muddy track, a commune has set up shop. They have a goat, a
Rhodes piano, and a deeply held conviction that the world can be fixed with **Free Love, Peace
and Fusion Jazz**. To spread the word they print a magazine — the *Wanneeikel Free Press* — and
this year they have gone fully electric: there is a web page now, where readers can read the
articles and leave their vibes in the comments.

It runs beautifully on the editor's machine. `./mvnw quarkus:dev` and there it is — the current
issue, the bound archive of back issues, the founding manifesto, the masthead with the thought
for the day.

Then someone decides the paper should boot in milliseconds and sip memory, so it can run on the
ancient donated server in the commune's pantry. They compile it to a **native binary**.

And the trouble begins. The binary builds. It starts. But the archive page throws a tantrum, the
manifesto has vanished into thin air, and the masthead insists the paper went to press at a time
that makes no sense at all. None of this happens in JVM mode. All of it happens in native.

Your job: get the paper running correctly as a native binary — without breaking it in JVM mode.


## What this project demonstrates

- The **closed-world assumption** of GraalVM native image: only what the build can prove
  reachable ends up in the binary. Anything resolved dynamically at runtime must be *declared*.
- Why code that works perfectly on the JVM can fail **only** once compiled to native.
- The two faces of native trouble: a **runtime failure** (something the build didn't know to
  include) versus **wrong behaviour** (something the build resolved too early).
- Running the same test suite against the JVM and against the native binary with
  `@QuarkusIntegrationTest`.


## What's already provided

A complete, working magazine — in JVM mode:

- `GET /articles`, `GET /articles/{id}`, `POST /articles/{id}/comments` — reading and commenting.
  These work in JVM **and** native; they are here so you have a baseline that simply works.
- `GET /archive` — citations for every back issue (Task 1).
- `GET /manifesto` — the commune's founding manifesto (Task 2).
- `GET /press` — the masthead: when the paper went to press, and the thought for the day (Task 3).
- A web page at `/` tying it all together.

`./mvnw test` is **green**. Every failure in this exercise appears only in the native binary.


## How verification works

The test suite in `FreePressTest` runs in JVM mode and passes. `NativeFreePressIT` extends that
same suite and re-runs it against the compiled native binary. Three of the tests fail there until
you fix the corresponding task:

| Task | Failing test (native only)         | Endpoint        |
|------|------------------------------------|-----------------|
| 1    | `archivePageListsBackIssues`       | `GET /archive`  |
| 2    | `manifestoIsPublished`             | `GET /manifesto`|
| 3    | `pressTimeReflectsThisRunOfThePaper` | `GET /press`  |

The native image is built **inside a container**, so you do **not** need a local GraalVM or
Mandrel installation — only a running Docker (or Podman). The build takes a few minutes.


## Tasks

> **Tip:** a full native build is slow. Fix all three tasks, then build once and run the native
> tests to confirm everything is green. You can reproduce each failure by hand in the meantime by
> visiting the endpoints in the running native binary (see *Running the native binary* below).

---

### Task 1 — The archive page is empty in native

**Failing test:** `archivePageListsBackIssues`

Open the running paper and click **The Archive**. In dev mode it lists every back issue the
commune ever printed. In the native binary the page is broken — the request comes back as a
server error, and the logs show the archive ledger blowing up the moment the paper tries to read
it. The exact same parsing code is happy on the JVM.

Make the archive page list the back issues when running as a native binary, just as it does in
JVM mode.

---

### Task 2 — The manifesto has vanished in native

**Failing test:** `manifestoIsPublished`

Click **Manifesto**. In dev mode you get the full founding text. In the native binary the paper
swears it cannot find the manifesto at all — even though the file is sitting right there in
`src/main/resources` and was clearly on the classpath a moment ago in JVM mode.

Make the manifesto readable from the native binary.

---

### Task 3 — The masthead is stuck in time

**Failing test:** `pressTimeReflectsThisRunOfThePaper`

The masthead reports when this run of the paper "went to press". In JVM mode that moment lines up
with when you actually started the application. In the native binary it is wildly off — the paper
claims to have gone to press long before you ever launched the binary, and the "thought for the
day" never changes no matter how often you restart. It is as if that part of the paper was
frozen at some earlier moment and never refreshed for the actual run.

Make the "went to press" moment (and the thought for the day) belong to the run that is actually
serving the page.


## Running the project

```bash
./mvnw quarkus:dev
```

Open http://localhost:8080. Everything works in dev mode — that is the point.


## Running the tests (JVM)

```bash
./mvnw test
```

All green. The native integration tests are skipped here.


## Running the native tests

Docker (or Podman) must be running.

```bash
./mvnw verify -Dnative
```

This builds the native binary in a container and runs `NativeFreePressIT` against it. On the
unmodified code, three tests fail — one per task. Make them pass without breaking `./mvnw test`.


## Running the native binary

To poke at the failures by hand:

```bash
./mvnw package -Dnative
./target/wanneeikel-free-press-1.0.0-SNAPSHOT-runner
```

Then open http://localhost:8080 and try the Archive, the Manifesto, and watch the masthead.
