# 05 — Persistence: The Grimoire Library

![Grimoire Libraray](grimoire-library.png)

## Grimoire Academy, Winter Holiday, Year 1421

The magical library of Grimoire Academy is the beating heart of the school. Every tome of
ancient spells, every chapter of forbidden lore, every borrowed scroll — all of it is
tracked by the library management system.

Or it was, until last Tuesday.

During the Great Holiday Feast, a third-year student accidentally cast a *Corrupto Maximus*
spell while reaching for the pudding. The library system took a direct hit. When the smoke
cleared, the catalogue was empty, chapters were vanishing, author searches returned nothing,
and the apprentice enrolment desk was throwing fits at anyone who tried to register.

The Head Librarian has declared a state of emergency. The winter holiday ends in three days.
Every apprentice needs their borrowed tomes recorded before term resumes, or the late fees
will be catastrophic.

Your task: restore the library system before the holiday is over.


## What this project demonstrates

- Defining JPA entities with `@Entity`, `@Id`, `@GeneratedValue`
- Mapping relationships with `@OneToMany`, `@ManyToOne`, and cascade operations
- Writing JPQL queries with `EntityManager`
- Using Panache active record (`PanacheEntity`) and repository (`PanacheRepository`) patterns
- Custom Panache finder methods
- Panache query pagination with `.page(index, size)`
- Loading initial data with `import.sql`
- Zero-config database setup in dev and test via Quarkus Dev Services


## Tasks

---

### Task 1 — The library opens empty

**Failing test:** `libraryHasFiveTomesOnStartup`

The library is supposed to open every morning with five foundational tomes already on the
shelves — the cornerstones of any magical education. Instead, the catalogue is completely
empty on every startup.

The recovered library catalogue has been found in `catalogue.sql` at the root of the project.
Find a way to make Quarkus load it automatically on startup. Make sure that registering new
tomes after startup works without errors.

---

### Task 2 — Chapters are invisible from the tome side

**Failing test:** `chaptersAreRetrievableViaTome`

Chapters are stored in the database and each one correctly references its tome. But when a
tome is loaded, its chapter list is always empty — the connection only works in one direction.

---

### Task 3 — Author search always returns nothing

**Failing test:** `searchingByAuthorReturnsMatchingTomes`

Searching for tomes by author always returns an empty list, even for authors whose works are
definitely in the catalogue. A direct lookup by ID works fine — the issue is specific to
the author search.

---

### Task 4 — Registering a new apprentice crashes the server

**Failing test:** `enrollingAnApprenticeSucceeds`

Every attempt to enrol a new apprentice fails with an internal server error. The application
log mentions a missing transactional context. No apprentice is ever saved, regardless of
what is sent in the request.

---

### Task 5 — House search always returns no apprentices

**Failing test:** `findingApprenticesByHouseReturnsCorrectResults`

Fetching the list of apprentices for any given house always returns an empty list, no matter
how many apprentices belong to that house. The endpoint accepts the house name and produces
a `200 OK`, but the list is always empty.

Note: complete Task 4 first so apprentices can be enrolled.

---

### Task 6 — Active loan tracking is backwards

**Failing test:** `activeLoansOnlyIncludesUnreturnedBooks`

The active loans list shows tomes that have already been returned to the shelf — and excludes
the ones that are still out on loan. The system has the logic exactly backwards. Apprentices
are being chased for books they already returned, while genuinely overdue tomes go unnoticed.

---

### Task 7 — Apprentice listing ignores page size

**Failing test:** `listingApprenticesRespectsPagination`

The apprentice list endpoint accepts `page` and `size` query parameters, but always returns
the entire roster regardless of what values are provided. With hundreds of apprentices
enrolled, this makes the endpoint unusable for the front desk terminals.

Note: complete Task 4 first so apprentices can be enrolled.

---


## Running the project

Docker must be running. Quarkus Dev Services will automatically start a PostgreSQL container
when you launch the application in dev mode — no database configuration required.

```bash
./mvnw quarkus:dev
```

The OpenAPI spec is at http://localhost:8080/q/openapi and Swagger UI at
http://localhost:8080/q/swagger-ui.


## Running the tests

Docker must be running. Dev Services also provides the test database automatically.

```bash
./mvnw test
```

Seven tests fail on the unmodified code. Complete Tasks 1–7 to make them pass.
