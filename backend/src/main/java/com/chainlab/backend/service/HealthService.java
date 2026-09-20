package com.chainlab.backend.service;

import com.chainlab.backend.repository.HealthRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class HealthService {

    @Inject
    HealthRepository healthRepository;

    public String status() {
        return healthRepository.ping() ? "OK" : "DOWN";
    }
}
