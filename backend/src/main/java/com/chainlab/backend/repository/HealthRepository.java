package com.chainlab.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HealthRepository {

    public boolean ping() {
        return true;
    }
}
