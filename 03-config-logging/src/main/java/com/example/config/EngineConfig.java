package com.example.config;

import io.smallrye.config.ConfigMapping;

/**
 * TODO Task 6: Turn this into a working {@link ConfigMapping}.
 *
 * <p>A ConfigMapping groups related configuration properties under a common prefix
 * and maps them to a typed Java interface. Quarkus generates the implementation at
 * build time — no boilerplate required.
 *
 * <p>Steps:
 * <ol>
 *   <li>Add {@code @ConfigMapping(prefix = "engine")} to this interface.</li>
 *   <li>Declare methods for every {@code engine.*} property currently injected
 *       via {@code @ConfigProperty} in {@link com.example.engine.AetherEngineService}
 *       and {@link com.example.engine.FuelRegulator}.</li>
 *   <li>Use nested interfaces for grouped sub-properties (pressure, fuel).
 *       Method names follow camelCase → kebab-case mapping automatically:
 *       {@code flowRate()} maps to {@code engine.fuel.flow-rate}.</li>
 *   <li>Inject this interface in {@code AetherEngineService} and replace the
 *       {@code @ConfigProperty} fields with calls to {@code engineConfig.pressure().min()},
 *       etc. Uncomment the lines in {@code getInfo()} marked with "Task 6".</li>
 * </ol>
 *
 * <p>Hint — nested interface example:
 * <pre>
 *   interface Pressure {
 *       int min();
 *       int max();
 *       int emergencyThreshold();
 *   }
 *   Pressure pressure();
 * </pre>
 */
public interface EngineConfig {
    // TODO: add @ConfigMapping(prefix = "engine") above the interface declaration
    // TODO: add method declarations for name, location, displayName,
    //       temperatureMax, and the nested Pressure and Fuel interfaces
}
