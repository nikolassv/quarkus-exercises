package com.example.engine;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * Main control service for the Great Aetheric Engine.
 */
@ApplicationScoped
public class AetherEngineService {

    private static final Logger LOG = Logger.getLogger(AetherEngineService.class);

    @ConfigProperty(name = "engine.display-name")
    String displayName;

    @ConfigProperty(name = "engine.name")
    String name;

    @ConfigProperty(name = "engine.location")
    String location;

    @ConfigProperty(name = "engine.pressure.min")
    int pressureMin;

    @ConfigProperty(name = "engine.pressure.max")
    int pressureMax;

    @ConfigProperty(name = "engine.pressure.emergency-threshold")
    int emergencyThreshold;

    @ConfigProperty(name = "engine.fuel.type")
    String fuelType;

    @ConfigProperty(name = "engine.temperature.max")
    int temperatureMax;

    @Inject
    FuelRegulator fuelRegulator;

    /**
     * Returns the current engine status based on configuration validity.
     */
    public String getStatus() {
        if (pressureMin >= pressureMax) {
            LOG.errorf("CRITICAL: Pressure configuration invalid — min (%d) >= max (%d). Engine halted.",
                    pressureMin, pressureMax);
            return "HALTED";
        }
        LOG.infof("Engine '%s' is operational. Pressure range: %d\u2013%d bar.",
                displayName, pressureMin, pressureMax);
        return "OPERATIONAL";
    }

    /**
     * Attempts to start the engine.
     */
    public String startEngine() {
        LOG.infof("Initiating startup sequence for %s...", displayName);

        if (pressureMin >= pressureMax) {
            LOG.error("Cannot start: pressure configuration is invalid.");
            return "START FAILED \u2014 invalid pressure configuration";
        }

        LOG.debugf("Activating fuel system. Fuel type: %s", fuelType);
        fuelRegulator.regulateFuel();

        LOG.infof("Engine started successfully. Operating at %d\u2013%d bar, max temperature %d\u00b0C.",
                pressureMin, pressureMax, temperatureMax);
        return "ENGINE STARTED \u2014 " + displayName + " is now OPERATIONAL";
    }

    /**
     * Returns a formatted configuration summary for the engine.
     */
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name    : ").append(name).append("\n");
        sb.append("Location: ").append(location).append("\n");
        sb.append("Display : ").append(displayName).append("\n");
        sb.append("Status  : ").append(getStatus()).append("\n");
        sb.append("Pressure: ").append(pressureMin).append("\u2013").append(pressureMax).append(" bar\n");
        sb.append("Fuel    : ").append(fuelType).append(" @ ").append(fuelRegulator.getFlowRate()).append(" units/s\n");
        sb.append("Temp max: ").append(temperatureMax).append("\u00b0C\n");

        // TODO Task 6: After implementing EngineConfig and injecting it here,
        //              replace the individual @ConfigProperty fields above

        return sb.toString();
    }
}
