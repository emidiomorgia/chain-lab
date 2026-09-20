package com.chainlab.backend.controller;

import com.chainlab.backend.service.HealthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/health")
public class HealthController {

    @Inject
    HealthService healthService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return healthService.status();
    }
}
