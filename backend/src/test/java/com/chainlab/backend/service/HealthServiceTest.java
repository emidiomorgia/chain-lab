package com.chainlab.backend.service;

import com.chainlab.backend.repository.HealthRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthServiceTest {

    @Mock
    HealthRepository healthRepository;

    @InjectMocks
    HealthService healthService;

    @Test
    void statusReturnsOkWhenRepositoryPingSucceeds() {
        when(healthRepository.ping()).thenReturn(true);

        String result = healthService.status();

        assertEquals("OK", result);
        verify(healthRepository).ping();
    }

    @Test
    void statusReturnsDownWhenRepositoryPingFails() {
        when(healthRepository.ping()).thenReturn(false);

        String result = healthService.status();

        assertEquals("DOWN", result);
        verify(healthRepository).ping();
    }
}
