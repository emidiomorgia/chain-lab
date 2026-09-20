package com.chainlab.backend.e2e;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

// Caso d'uso: verifica dello stato di salute tramite l'endpoint REST reale.
@QuarkusTest
class HealthEndpointE2ETest {

    @Test
    void getHealthReturnsOk() {
        given()
            .when().get("/health")
            .then()
                .statusCode(200)
                .body(equalTo("OK"));
    }
}
