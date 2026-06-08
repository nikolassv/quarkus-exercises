# 06 — Reactive: UhOh Messenger

![UhOh dorm room](uhoh-dorm-room.png "two CRT monitors, beige carpet, a 56k modem doing the handshake")

## Pittsburgh, October 1999

You and your roommate just installed the ICQ client for the fourteenth time this semester (the
floppy keeps eating it). The whole dorm is buzzing about that little flower icon. People are
leaving their doors open just to hear the *"Uh-oh!"* chime ripple down the hallway.

You both look at each other.

> *"I bet we could build that. It's just a server that fans messages out, right?"*

By midnight you have the broadcasting machinery: a server that accepts raw messages and fans them
out to every connected listener. By 2am you have a UI in beige and teal. By 2:30am you notice that
when you send a message from one window, it appears... in no window at all. The pipe is wide open
between sender and broadcaster, and between broadcaster and listener — but in the middle, where
the raw messages should be turned into something the world can read, there is a black hole.

Your job: build the pipeline that turns raw, unstamped, possibly-blank user input into clean,
timestamped events that go out over Server-Sent Events. If you succeed, the dorm gets its
messaging app. If you don't, you go back to passing notes under doors.


## What this project demonstrates

- Mutiny `Multi<T>` as the streaming primitive — "more than one item, over time"
- Composing a `Multi` pipeline with `onItem().transform(...)`, `select().where(...)`,
  `onItem().invoke(...)`, and friends
- Returning a `Multi<T>` directly from a JAX-RS resource method
- Server-Sent Events (SSE) as the HTTP delivery channel —
  `@Produces(MediaType.SERVER_SENT_EVENTS)` plus `@RestStreamElementType`
- A **hot** stream (the broadcaster, provided): one source fans out to every connected subscriber
- Consuming SSE from the browser via `EventSource`


## What's already provided

`ChatBroadcaster` is a complete, working hot stream. You can publish raw events into it from
anywhere, and any number of subscribers reading from it will receive every event emitted after
they subscribed.

- `broadcaster.publishMessage(RawMessage raw)` — push a raw message into the bus
- `broadcaster.rawMessages()` — `Multi<RawMessage>` that fans out to every subscriber
- `broadcaster.publishPresence(RawPresence raw)` / `broadcaster.rawPresence()` — same idea for
  presence events
- `broadcaster.currentlyOnline()` — a `Collection<String>` snapshot of the senders who are
  currently online (have JOINED and not yet LEFT)

Both REST resources are already wired: `POST /messages` and `POST /presence` push into the
broadcaster, and both SSE endpoints exist. **What's missing is the bit in between** — the
pipeline that turns the raw inputs into the final events SSE should deliver.


## Tasks

---

### Task 1 — Build the message pipeline

**Failing tests:** `subscriberReceivesPostedMessage`, `messageTextIsTrimmed`,
`blankMessagesAreDroppedAndStreamSurvives`, `nullMessageTextIsDroppedAndStreamSurvives`

`ChatResource#stream()` returns `Multi.createFrom().nothing()`. Subscribers connect, hear silence,
and start to wonder if the server is still alive.

Replace the body of `stream()` with a Mutiny pipeline that starts from `broadcaster.rawMessages()`
and produces a `Multi<Message>` where every emitted message:

- has its `text` trimmed of leading and trailing whitespace,
- has its `sentAt` set to a server-side timestamp,
- has real, non-blank text — messages where `text` is missing, `null`, or blank after trimming
  must be **dropped**, not forwarded. Dropping one bad item must not stop later items from
  flowing (and remember: an uncaught exception in a Mutiny operator *terminates the whole
  stream*, so all your subscribers will lose their connection).

You may also want to log every message that passes through, for sanity.

---

### Task 2 — Build the presence pipeline

**Failing tests:** `joinEventIsBroadcastToSubscribers`,
`invalidPresenceKindIsDroppedAndStreamSurvives`, `nullPresenceKindIsDroppedAndStreamSurvives`

`PresenceResource#stream()` has the same problem as Task 1, on the presence stream.

Replace the body of `stream()` with a Mutiny pipeline that starts from `broadcaster.rawPresence()`
and produces a `Multi<Presence>` where every emitted event:

- has a `kind` parsed from the incoming string into the `PresenceKind` enum (`JOINED`,
  `TYPING`, or `LEFT`),
- has its `at` set to a server-side timestamp,
- is **dropped** if the incoming `kind` is missing, `null`, or anything other than one of those
  three values — and one bad event must not stop later events from flowing.

---

### Task 3 — New arrivals see an empty room

**Failing tests:** `newSubscriberSeesAlreadyOnlineUsers`, `newSubscriberDoesNotSeeUsersWhoLeft`

Once Task 2 works, try this: open one browser window and let a few buddies join. Now open a
*second* window. Its contact list is empty — it only fills up as *new* people join or type. Every
buddy who was already online before you connected is invisible to you, even though the server
knows perfectly well that they are there.

That is the nature of the stream you are subscribed to: it is **hot**. It carries events from the
moment you subscribe onwards — it does not replay the ones you missed. The live stream alone can
never tell a newcomer who is *already* here.

Make a freshly connected subscriber receive the current roster first, and then the live updates.
The list of who is online right now is available to you; the events that follow should continue
to flow exactly as before.

---

### Manual verification

Open `http://localhost:8080` in two browser windows.

- Send a message in one — it should appear in both.
- Type without pressing Send — the other window should show *"X is typing..."* for a couple of
  seconds.
- Close one window — the other should see that buddy disappear from the contact list.
- Let a buddy join, then open a fresh window — the newcomer should see that buddy already in the
  contact list (Task 3).


## Running the project

```bash
./mvnw quarkus:dev
```

Open http://localhost:8080.


## Running the tests

```bash
./mvnw test
```

Nine tests fail on the unmodified code: four for Task 1, three for Task 2, two for Task 3.
