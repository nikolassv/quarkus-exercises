package com.example.freepress;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;

/**
 * Behavioural tests for the Wanneeikel Free Press.
 *
 * <p>Every test here passes in JVM mode ({@code ./mvnw test}). The same tests are re-run against
 * the compiled native binary by {@link NativeFreePressIT}, where three of them fail until the
 * corresponding task is solved.
 */
@QuarkusTest
class FreePressTest {

    // --- Baseline: the parts of the paper that work everywhere ----------------------------

    @Test
    void currentIssueListsAllArticles() {
        given()
                .when().get("/articles")
                .then()
                .statusCode(200)
                .body("$", hasSize(4));
    }

    @Test
    void readersCanLeaveAComment() {
        given()
                .contentType("application/json")
                .body("{\"author\":\"Moonbeam\",\"text\":\"Far out, man.\"}")
                .when().post("/articles/1/comments")
                .then()
                .statusCode(201);

        given()
                .when().get("/articles/1")
                .then()
                .statusCode(200)
                .body("comments.text", hasItem("Far out, man."));
    }

    // --- Task 1: the archive page ---------------------------------------------------------

    @Test
    void archivePageListsBackIssues() {
        given()
                .when().get("/archive")
                .then()
                .statusCode(200)
                .body("$", hasItem(containsString("Coltrane Came to Me in a Dream")));
    }

    // --- Task 2: the manifesto ------------------------------------------------------------

    @Test
    void manifestoIsPublished() {
        given()
                .when().get("/manifesto")
                .then()
                .statusCode(200)
                .body(containsString("THE WANNEEIKEL MANIFESTO"));
    }

    // --- Task 3: the masthead -------------------------------------------------------------

    @Test
    void pressTimeReflectsThisRunOfThePaper() {
        // The moment the paper "went to press" should belong to this run — i.e. it should sit
        // right next to when the office opened, not minutes or hours earlier.
        given()
                .when().get("/press")
                .then()
                .statusCode(200)
                .body("driftSeconds", lessThan(60));
    }
}
