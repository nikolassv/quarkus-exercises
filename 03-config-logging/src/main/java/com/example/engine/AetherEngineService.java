package com.example.engine;

import com.example.config.EngineConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

/**
 * Main control service for the Great Aetheric Engine.
 */
@ApplicationScoped
public class AetherEngineService {

    private static final Logger LOG = Logger.getLogger(AetherEngineService.class);

    @Inject
    EngineConfig engineConfig;


    @Inject
    FuelRegulator fuelRegulator;

    /**
     * Returns the current engine status based on configuration validity.
     */
    public String getStatus() {
        if (engineConfig.pressure().min() >= engineConfig.pressure().max()) {
            LOG.errorf("CRITICAL: Pressure configuration invalid — min (%d) >= max (%d). Engine halted.",
                    engineConfig.pressure().min(), engineConfig.pressure().max());
            return "HALTED";
        }
        LOG.infof("Engine '%s' is operational. Pressure range: %f\u2013%f bar.",
                engineConfig.displayName(), engineConfig.pressure().min(), engineConfig.pressure().max());
        return "OPERATIONAL";
    }

    /**
     * Attempts to start the engine.
     */
    public String startEngine() {
        LOG.infof("Initiating startup sequence for %s...", engineConfig.displayName());

        if (engineConfig.pressure().min() >= engineConfig.pressure().max()) {
            LOG.error("Cannot start: pressure configuration is invalid.");
            return "START FAILED \u2014 invalid pressure configuration";
        }

        LOG.debugf("Activating fuel system. Fuel type: %s", engineConfig.fuel().type());
        fuelRegulator.regulateFuel();

        LOG.infof("Engine started successfully. Operating at %f\u2013%f bar, max temperature %d\u00b0C.",
                engineConfig.pressure().min(), engineConfig.pressure().max(), engineConfig.temperature().max());
        return "ENGINE STARTED \u2014 " + engineConfig.displayName() + " is now OPERATIONAL";
    }

    /**
     * Returns a formatted configuration summary for the engine.
     */
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name    : ").append(engineConfig.name()).append("\n");
        sb.append("Location: ").append(engineConfig.location()).append("\n");
        sb.append("Display : ").append(engineConfig.displayName()).append("\n");
        sb.append("Status  : ").append(getStatus()).append("\n");
        sb.append("Pressure: ").append(engineConfig.pressure().min()).append("\u2013").append(engineConfig.pressure().max()).append(" bar\n");
        sb.append("Fuel    : ").append(engineConfig.displayName()).append(" @ ").append(fuelRegulator.getFlowRate()).append(" units/s\n");
        sb.append("Temp max: ").append(engineConfig.temperature().max()).append("\u00b0C\n");

        return sb.toString();
    }
}
