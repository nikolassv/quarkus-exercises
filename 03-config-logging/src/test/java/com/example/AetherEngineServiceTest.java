package com.example;

import com.example.engine.AetherEngineService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class AetherEngineServiceTest {

    @Inject
    AetherEngineService engineService;

    @ConfigProperty(name = "engine.location")
    String location;

    @ConfigProperty(name = "engine.display-name")
    String displayName;

    /**
     * Task 1 — The engine should be operational once the configuration is correct.
     */
    @Test
    void engineShouldBeOperational() {
        assertThat(engineService.getStatus()).isEqualTo("OPERATIONAL");
    }

    /**
     * Task 2 — The display name should reflect the current city name.
     */
    @Test
    void displayNameShouldReflectLocation() {
        assertThat(displayName).contains(location);
    }
}
