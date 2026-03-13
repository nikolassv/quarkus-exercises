package com.example.engine;

import com.example.config.EngineConfig;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Regulates the flow of aetheric fuel into the engine boiler.
 */
@ApplicationScoped
public class FuelRegulator {

    private static final double MAX_SAFE_FLOW_RATE = 5.0;

    @Inject
    EngineConfig engineConfig;

    public void regulateFuel() {
        if (engineConfig.fuel().flowRate() > MAX_SAFE_FLOW_RATE) {
            Log.errorf("WARNING: Flow rate %f exceeds safe maximum of %f!", engineConfig.fuel().flowRate(), MAX_SAFE_FLOW_RATE);
        }
        Log.infof("INFO: Regulating fuel for %s with rate %f",
                engineConfig.fuel().type(), engineConfig.fuel().flowRate());
    }

    public double getFlowRate() {
        return engineConfig.fuel().flowRate();
    }
}
