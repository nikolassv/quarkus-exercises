package com.example.engine;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Regulates the flow of aetheric fuel into the engine boiler.
 *
 * <p>TODO Task 4: This class uses {@code System.err.println} for its warning output,
 * which bypasses the application's logging infrastructure entirely. Add proper logging.
 */
@ApplicationScoped
public class FuelRegulator {

    private static final double MAX_SAFE_FLOW_RATE = 5.0;

    @ConfigProperty(name = "engine.fuel.type")
    String fuelType;

    @ConfigProperty(name = "engine.fuel.flow-rate")
    double flowRate;

    public void regulateFuel() {
        if (flowRate > MAX_SAFE_FLOW_RATE) {
            System.err.println("WARNING: Flow rate " + flowRate
                    + " exceeds safe maximum of " + MAX_SAFE_FLOW_RATE + "!");
        }

        System.out.println("INFO: Regulating fuel for " + fuelType + " with rate " + flowRate);
    }

    public double getFlowRate() {
        return flowRate;
    }
}
