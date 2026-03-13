# 03 — Configuration & Logging: The Great Aetheric Engine

![The Machine is Broken](ironholms-machine.png "The Machine is Broken")

## The City of Cogsworth Needs You

The city of Cogsworth runs on a single colossal machine buried beneath its streets — the
*Great Aetheric Engine*. It provides power, heat, and light to every household and workshop
in the city. Its control system is a Quarkus application called the **Aetheric Engine Control
System (AECS)**.

A frantic telegraph arrives at your desk:

> *"The engine went silent before dawn. The city is running on emergency capacitors — roughly
> six hours of reserves remain. A technician changed the control system configuration before
> leaving and we cannot reach him. The logs show almost nothing. We cannot see what is wrong.
> You are our last hope."*

You boot up the AECS. The application starts, but the engine remains **HALTED**. The logs are
nearly silent. The configuration is broken in several places.

Your mission: diagnose the misconfiguration, fix it, and restore the engine before Cogsworth
goes dark.


## What this project demonstrates

**Configuration**
- Standard configuration via `application.properties`
- Build-time vs runtime configuration properties
- Property expressions (referencing one property from another)
- Multiple configuration sources and their priority order
- `@ConfigMapping` — grouping related properties into a typed interface
- Profiles (`%dev`, `%prod`) for environment-specific values

**Logging**
- Adding a Logger and logging at the correct level
- Log levels and the logging hierarchy
- Configuring the console log handler
- Persisting logs with the file log handler


## Tasks

Work through these in order. Tasks 1 and 2 have failing tests — run `./mvnw test` to see them
fail, then fix the configuration and watch them go green. The remaining tasks have no automated
test; verify them by running the application and observing the logs or the control panel.

---

### Task 1 — Fix the pressure configuration

**Failing test:** `AetherEngineServiceTest.engineShouldBeOperational`

The engine is reporting the wrong status. Something in the configuration is preventing it
from reaching an operational state.

---

### Task 2 — Fix the city name

**Failing test:** `AetherEngineServiceTest.displayNameShouldReflectLocation`

The display name shown in the control panel still uses the city's old name — Ironholm —
before it was renamed to Cogsworth. Fix the inconsistency.

Once you have, take a look at how many places in `application.properties` contain the city
name. The configuration system supports a way to define a value once and reference it from
other properties — worth considering if the city ever votes on another renaming.

---

### Task 3 — Make the logs useful

The logs are nearly silent. Start the application and try the startup sequence from the
control panel — you should be able to follow what the engine is doing. Adjust the logging
configuration until you can.

---

### Task 4 — Add proper logging to `FuelRegulator`

Open `FuelRegulator.java`. Its warning output bypasses the application's logging
infrastructure entirely — no timestamps, no log levels, no routing to handlers. Replace
it with proper logging.

---

### Task 5 — Persist logs to disk

Engine logs are lost on every restart, making post-incident analysis difficult. Configure
the logging system to write logs to a file named `engine.log` in the `log` path. Also enable per-boot rotation
with a date suffix — the Cogsworth Incident Review Board requires each startup to produce its
own timestamped log file so failures can be traced back to the exact run.

---

### Task 6 — Introduce a ConfigMapping

The manifest endpoint currently reads configuration through many individual `@ConfigProperty`
fields scattered across the service. Quarkus supports a more structured alternative for
grouped properties.

Migrate `AetherEngineService` to use it, and extend the manifest output to include the
pressure and fuel subsystem details.

---

### Task 7 — Add a `%prod` profile and experiment with config sources

Profiles allow different configuration values per environment. Add a `%prod` profile entry
to `application.properties` for `engine.temperature.max`.

Then explore whether you can override that value without modifying the properties file at all.


## Running the project

```bash
./mvnw quarkus:dev
```

Open [http://localhost:8080](http://localhost:8080) for the **Aetheric Engine Control Panel** —
use it to poll the engine status, trigger the startup sequence, and inspect the configuration
manifest.

## Running the tests

```bash
./mvnw test
```

Two tests fail on the unmodified code. Complete Tasks 1 and 2 to make them pass.
