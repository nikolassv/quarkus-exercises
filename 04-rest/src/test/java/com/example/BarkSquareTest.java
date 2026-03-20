package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class BarkSquareTest {

    // -------------------------------------------------------------------------
    // Task 1 — POST /pets should return 201 Created
    // -------------------------------------------------------------------------

    @Test
    void registeringAPetReturns201() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "Fido",
                        "species", "DOG",
                        "ownerHandle", "@tester",
                        "bio", "Good boy. Available for fetch."
                ))
                .when().post("/pets")
                .then()
                .statusCode(201);
    }

    // -------------------------------------------------------------------------
    // Task 2 — GET /pets/{id} should return the correct pet
    // -------------------------------------------------------------------------

    @Test
    void fetchingAPetProfileById() {
        given()
                .when().get("/pets/biscuit")
                .then()
                .statusCode(200)
                .body("name", equalTo("Biscuit"))
                .body("species", equalTo("DOG"));
    }

    // -------------------------------------------------------------------------
    // Task 3 — GET /squeaks?species=CAT should only return cat squeaks
    // -------------------------------------------------------------------------

    @Test
    void searchingBySpeciesFilters() {
        // Cats in the seed data: mittens, professor-whiskers
        given()
                .when().get("/squeaks?species=CAT")
                .then()
                .statusCode(200)
                .body("petId", everyItem(anyOf(
                        equalTo("mittens"),
                        equalTo("professor-whiskers")
                )));
    }

    // -------------------------------------------------------------------------
    // Task 4 — POST /squeaks with X-Pet-Id header should create a squeak
    // -------------------------------------------------------------------------

    @Test
    void postingSqueakWithPetIdHeaderCreates() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Pet-Id", "biscuit")
                .body(Map.of("content", "Just found the best stick. 10/10 would fetch again."))
                .when().post("/squeaks")
                .then()
                .statusCode(201)
                .body("petId", equalTo("biscuit"))
                .body("content", containsString("stick"));
    }

    // -------------------------------------------------------------------------
    // Task 5 — GET /squeaks/my should read the pet_session cookie
    // -------------------------------------------------------------------------

    @Test
    void myFeedReadsPetSession() {
        given()
                .cookie("pet_session", "biscuit")
                .when().get("/squeaks/my")
                .then()
                .statusCode(200)
                .body("$.size()", greaterThan(0))
                .body("petId", everyItem(equalTo("biscuit")));
    }

    // -------------------------------------------------------------------------
    // Task 6 — GET /pets/{unknown} should return 404, not 500
    // -------------------------------------------------------------------------

    @Test
    void unknownPetReturns404() {
        given()
                .when().get("/pets/definitely-not-a-real-pet-id")
                .then()
                .statusCode(404);
    }

    // -------------------------------------------------------------------------
    // Task 7 — GET /pets/{id} should also work with Accept: text/plain
    // -------------------------------------------------------------------------

    @Test
    void petProfileAsTextReturnsFormattedCard() {
        given()
                .accept(ContentType.TEXT)
                .when().get("/pets/biscuit")
                .then()
                .statusCode(200)
                .contentType(containsString("text/plain"))
                .body(containsString("Biscuit"));
    }

    // -------------------------------------------------------------------------
    // Task 9 — Write a REST-assured test, then implement the validation
    // -------------------------------------------------------------------------

    @Test
    void squeaksAreRejectedIfTooLong() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Pet-Id", "biscuit")
                .body(Map.of("content", """
                        Borkem ipsum dolor sit amet, consectetur wagging elit, sed do eiusmod
                        tempor incididunt ut treat et slobber magna aliqua. Ut enim ad minim
                        venibork, quis nostrud exercitation ullamco fetch laboris nisi ut aliquip
                        ex ea commodo chew. Duis aute irure dolor in reprehenderit in voluptate
                        velit esse cillum sniffle eu fugiat nulla pawriatur. Excepteur sit
                        occaecat cupidatat non proident, sunt in culpa qui officia deserunt
                        mollit anim id est roll-over.
                        """))
                .when().post("/squeaks")
                .then()
                .statusCode(400);
    }
}
