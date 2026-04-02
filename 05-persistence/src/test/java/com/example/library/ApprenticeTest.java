package com.example.library;

import com.example.library.model.Apprentice;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
class ApprenticeTest {

    @BeforeEach
    void cleanup() {
        QuarkusTransaction.requiringNew().run(() -> Apprentice.deleteAll());
    }

    @Test
    void enrollingAnApprenticeSucceeds() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "Lyra Moonfall", "house", "Tidesong", "enrollmentYear", 1421))
                .when().post("/apprentices")
                .then()
                .statusCode(201);
    }

    @Test
    void findingApprenticesByHouseReturnsCorrectResults() {
        given().contentType(ContentType.JSON)
                .body(Map.of("name", "Kael Stonehaven", "house", "Ironroot", "enrollmentYear", 1420))
                .post("/apprentices");
        given().contentType(ContentType.JSON)
                .body(Map.of("name", "Sira Dawnfire", "house", "Emberhold", "enrollmentYear", 1420))
                .post("/apprentices");

        given()
                .when().get("/apprentices/house/Ironroot")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo("Kael Stonehaven"));
    }

    @Test
    void listingApprenticesRespectsPagination() {
        for (int i = 1; i <= 5; i++) {
            given().contentType(ContentType.JSON)
                    .body(Map.of("name", "Apprentice " + i, "house", "Stormveil", "enrollmentYear", 1420 + i))
                    .post("/apprentices");
        }

        given()
                .queryParam("page", 0)
                .queryParam("size", 2)
                .when().get("/apprentices")
                .then()
                .statusCode(200)
                .body("$", hasSize(2));
    }
}
