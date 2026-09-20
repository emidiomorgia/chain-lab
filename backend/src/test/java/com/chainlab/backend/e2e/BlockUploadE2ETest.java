package com.chainlab.backend.e2e;

import com.chainlab.backend.repository.BlockRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Caso d'uso: upload di un file, che viene persistito come blocco della blockchain.
@QuarkusTest
class BlockUploadE2ETest {

    @Inject
    BlockRepository blockRepository;

    @BeforeEach
    void cleanCollection() {
        blockRepository.deleteAll();
    }

    @Test
    void uploadValidFileReturnsCreatedAndPersistsGenesisAndBlock() {
        given()
            .multiPart("file", "hello.txt", "hello world".getBytes())
            .when().post("/blocks")
            .then()
                .statusCode(201)
                .body("index", equalTo(1));

        assertEquals(2, blockRepository.count());
    }

    @Test
    void uploadWithoutFilePartReturnsBadRequest() {
        given()
            .multiPart("other", "irrelevant")
            .when().post("/blocks")
            .then()
                .statusCode(400);

        assertEquals(0, blockRepository.count());
    }

    @Test
    void uploadWithEmptyFileReturnsBadRequest() {
        given()
            .multiPart("file", "empty.txt", new ByteArrayInputStream(new byte[0]))
            .when().post("/blocks")
            .then()
                .statusCode(400);

        assertEquals(0, blockRepository.count());
    }
}
