# 04 — REST: BarkSquare

![BarkSquare Office](bark-square-office.png "busy developers in BarkSquares main office")

## San Francisco, March 2011

Three Stanford grads. One bold vision. Zero experience with HTTP beyond "the browser uses it."

BarkSquare — *"LinkedIn, but for your dog"* — is the hottest pre-seed startup in SoMa. The
pitch deck has 47 slides. A partner at a Sand Hill VC firm saw a demo last week and asked one
question before leaving:

> *"Do you have a web API? Everything is going AJAX. Google proved it with Maps and Gmail.
> REST is the future — are you REST?"*

The team googled "REST API" that afternoon. One engineer read Roy Fielding's dissertation (the
abstract). Another read a blog post. By 6pm they had written the BarkSquare REST API. By
6:30pm they had deployed it.

It does not work.

Your job: fix the BarkSquare API before the board demo on Friday. If you succeed, the team
closes the seed round. If you don't, they pivot to QR codes for dogs.


## What this project demonstrates

- Defining REST resources with `@Path`, `@GET`, `@POST`
- Reading request data: path params (`@PathParam`), query params (`@QueryParam`),
  headers (`@HeaderParam`), cookies (`@CookieParam`)
- Returning correct HTTP responses with `jakarta.ws.rs.core.Response`
- Content negotiation with `@Produces` and `@Consumes`
- Mapping exceptions to HTTP responses with `ExceptionMapper`
- Generating an OpenAPI specification with SmallRye OpenAPI
- Integration testing REST endpoints with REST-assured


## Tasks

---

### Task 1 — Registration returns the wrong status

**Failing test:** `registeringAPetReturns201`

The API accepts new pet registrations, but the response status code is wrong. HTTP defines a
specific code that means "a new resource was successfully created" — the API is sending a
different one instead.

---

### Task 2 — Profile lookup always fails

**Failing test:** `fetchingAPetProfileById`

Requesting `GET /pets/biscuit` always crashes, even though Biscuit definitely exists. The pet
ID is in the URL, but it never reaches the code that looks up the pet.

---

### Task 3 — The squeak feed ignores the filter

**Failing test:** `searchingBySpeciesFilters`

`GET /squeaks?species=CAT` should return only cat squeaks, but it always returns the entire
feed regardless of what species is requested.

---

### Task 4 — Posting a squeak is always rejected

**Failing test:** `postingSqueakWithPetIdHeaderCreates`

Every attempt to post a squeak is rejected with `400 Bad Request`. No squeak is ever created,
regardless of what the client sends.

---

### Task 5 — My feed is always empty

**Failing test:** `myFeedReadsPetSession`

`GET /squeaks/my` should return squeaks from the pet identified by the `pet_session` cookie.
It always returns an empty list, even when the cookie is set.

---

### Task 6 — Missing pet crashes the server

**Failing test:** `unknownPetReturns404`

Requesting a pet that doesn't exist returns `500 Internal Server Error`. The API should
instead communicate clearly that the resource was not found.

<details>
<summary>Hint</summary>

There is no need to modify `PetResource` to complete this task.

</details>

---

### Task 7 — Pet profiles only come in one flavour

**Failing test:** `petProfileAsTextReturnsFormattedCard`

A text-only client requests a pet profile with `Accept: text/plain`. The API rejects it with
`406 Not Acceptable`. Add plain text support to the pet profile endpoint.

---

### Task 8 — The API has no documentation

The investor's engineering advisor wants to review the API contract before the board meeting.
The SmallRye OpenAPI extension is already included in the project. Add `@Tag`, `@Operation`,
and `@APIResponse` annotations to the resource classes and verify the result at `/q/swagger-ui`.

---

### Task 9 — Write a REST-assured test

BarkSquare enforces a 140-character limit per squeak (hats off to the bird site). There is a
`TODO` comment in `BarkSquareTest`. Write the test first, then add the validation to the
endpoint.


## Running the project

```bash
./mvnw quarkus:dev
```

Open http://localhost:8080 for the BarkSquare developer console.

The OpenAPI spec is at http://localhost:8080/q/openapi and Swagger UI at http://localhost:8080/q/swagger-ui.

## Running the tests

```bash
./mvnw test
```

Seven tests fail on the unmodified code. Complete Tasks 1–7 to make them pass.
