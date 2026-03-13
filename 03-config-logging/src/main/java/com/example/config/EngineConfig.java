package com.example.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "engine")
public interface EngineConfig {
    String location();
    String name();
    String displayName();
    String managementContact();
    String broadcastStation();
    String maintenanceAuthority();

    PressureConfiguration pressure();
    FuelConfiguration fuel();
    TemperatureLimits temperature();

    interface PressureConfiguration {
        int min();
        int max();
        double emergencyThreshold();
    }

    interface FuelConfiguration {
        String type();
        double flowRate();
    }

    interface TemperatureLimits {
        int max();
    }
}
