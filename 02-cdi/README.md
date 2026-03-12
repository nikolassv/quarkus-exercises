# 02 — CDI: Starfleet Communications

## Stardate 44001.4

Starfleet Command has tasked you with completing the `StarfleetCommunicationService` — the
backbone of inter-ship communication across the quadrant. The system must route messages
through the correct channel, broadcast fleet-wide alerts, log every transmission per
Regulation 47, and suppress duplicate distress signals to prevent comm-channel saturation.

The codebase is partially implemented. Several components are incomplete or incorrectly
configured. Your mission: get all tests green before the Romulans notice.

## What this project demonstrates

- Injection points (field and constructor)
- Qualifiers and `@Default` / `@Any`
- `Instance<T>` for iterating all beans of a type
- Producer methods
- Bean scopes (`@ApplicationScoped`, `@RequestScoped`, `@Singleton`)
- Build-profile-dependent beans (`@IfBuildProfile`)
- Interceptors
- Decorators
- Events and observers
- Mocking CDI beans in tests with `@InjectMock` and Mockito

## Tasks

Work through these roughly in order. Some Tasks have failing tests. Others have no
automated test — verify them by running the application and inspecting the logs.

---

### Resolve the ambiguous sender injection (Part 1)

`SubspaceSender` and `WarpBeaconSender` both implement `MessageSender`. CDI cannot decide
which one to inject when a `MessageSender` is requested — it will refuse to start with an
ambiguous dependency error.

Create qualifier annotations (one per sender) and apply them to:
- the sender classes
- the corresponding injection points in `StarfleetCommunicationService`

**Failing tests:** all tests fail at deployment until this is fixed.

---

### Implement `broadcast()` (Part 1)

`StarfleetCommunicationService.broadcast()` should send the message to every available
`MessageSender`. A CDI `Instance<MessageSender>` is already injected — use it.

**Failing test:** `broadcastSendsToAllChannels`

---

### Fix the scope of `ShipContext` (Part 1)

`ShipContext` holds per-request state but its current scope causes all requests to share the
same instance. Read the TODO comment, identify the problem, and pick the correct scope.

**Verify:** start the app and open http://localhost:8080. Send a transmission from
"USS Enterprise", then click **Check context** — with the wrong scope the ship name leaks
into the unrelated context request. After the fix, **Check context** always returns null.

---

### Activate `HolodeckSimulatedSender` in dev mode (Part 1)

`HolodeckSimulatedSender` is a fully functional test double that prints transmissions to
stdout instead of sending real signals. Add the annotation that restricts it to the `dev`
build profile so it does not appear in production.

Verify: run `./mvnw quarkus:dev` — `HolodeckSimulatedSender` should appear among the senders
when broadcasting.

---

### Fire a `TransmissionSentEvent` (Part 2)

After every `transmit()` call, a `TransmissionSentEvent` should be fired so other
components can react. The `TransmissionSentEvent` record and `transmissionEvent` injection
point are not set up yet — add them yourself.

**Failing test:** `transmitFiresEventAndUpdatesLog` (together with task 4)

---

### Observe the `TransmissionSentEvent` (Part 2)

`StarfleetCommunicationsLog.onTransmission()` already contains the logging logic, but it
is never called because one annotation is missing from its parameter.

**Failing test:** `transmitFiresEventAndUpdatesLog`

---

### Activate the `DeduplicationDecorator` (Part 2)

`DeduplicationDecorator` already contains the deduplication logic but is excluded from CDI
with `@Vetoed`. Read the TODO comment in the class and turn it into a proper CDI decorator.

**Failing test:** `duplicateTransmissionIsSentOnlyOnce`

---

### Activate the `StarfleetAuditInterceptor` (Part 2)

`StarfleetAuditInterceptor` has the audit logic in place but is not wired as an interceptor.
Add the required CDI annotations to make it intercept every class and method marked with `@StarfleetAudit`.
Intercept all methods in the `StarfleetCommuniationService`.

**Verify:** start the application (`./mvnw quarkus:dev`) and open http://localhost:8080.
Send a transmission via the console — you should see `[STARFLEET AUDIT]` lines in the log.

---

## Running the project

```bash
./mvnw quarkus:dev
```

Open http://localhost:8080 for the Starfleet Communications Console — a minimal UI for
sending transmissions and observing the interceptor and scope behaviour in the logs.

## Running the tests

```bash
./mvnw test
```
