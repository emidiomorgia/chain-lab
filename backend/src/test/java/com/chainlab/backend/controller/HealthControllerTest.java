package com.chainlab.backend.controller;

import com.chainlab.backend.service.HealthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @Mock
    HealthService healthService;

    @InjectMocks
    HealthController healthController;

    @Test
    void healthDelegatesToServiceAndReturnsItsResult() {
        when(healthService.status()).thenReturn("OK");

        String result = healthController.health();

        assertEquals("OK", result);
        verify(healthService).status();
    }
}
