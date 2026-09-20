package com.chainlab.backend.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthRepositoryTest {

    @Test
    void pingReturnsTrue() {
        HealthRepository healthRepository = new HealthRepository();

        assertTrue(healthRepository.ping());
    }
}
