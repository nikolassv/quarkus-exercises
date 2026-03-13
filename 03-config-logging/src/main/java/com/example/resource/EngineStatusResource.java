package com.example.resource;

import com.example.engine.AetherEngineService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST resource exposing engine status and control endpoints.
 */
@Path("/engine")
public class EngineStatusResource {

    @Inject
    AetherEngineService engineService;

    /**
     * Returns the current engine status: OPERATIONAL or HALTED.
     */
    @GET
    @Path("/status")
    @Produces(MediaType.TEXT_PLAIN)
    public String status() {
        return engineService.getStatus();
    }

    /**
     * Attempts to start the engine and returns a result message.
     */
    @POST
    @Path("/start")
    @Produces(MediaType.TEXT_PLAIN)
    public String start() {
        return engineService.startEngine();
    }

    /**
     * Returns a formatted configuration summary of the engine.
     *
     * <p>After completing Task 6 (ConfigMapping), pressure and fuel
     * subsystem details sourced from {@code EngineConfig} will appear here.
     */
    @GET
    @Path("/info")
    @Produces(MediaType.TEXT_PLAIN)
    public String info() {
        return engineService.getInfo();
    }
}
