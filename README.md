# Quarkus Academy Examples

A collection of small, self-contained Quarkus projects for use in a developer course. Each project demonstrates a specific Quarkus feature or concept and includes practical tasks for participants to implement or fix.

## Topics

The following topics are covered (subject to change):

| Directory            | Topic                                              | Scenario                  |
|----------------------|----------------------------------------------------|---------------------------|
| `01-setup/`          | Setting up a basic Quarkus project                 | —                         |
| `02-cdi/`            | Contexts and Dependency Injection (CDI)            | Starfleet Communications  |
| `03-config-logging/` | Configuration with MicroProfile Config and Logging | The Great Aetheric Engine |
| `04-rest/`           | Building REST APIs                                 | BarkSquare                |
| `05-persistence/`    | Persistence with Hibernate ORM and Panache         | The Grimoire Library      |
| `06-reactive/`       | Reactive programming                               | UhOh Messenger            |
| `07-native/`         | Native compilation (GraalVM native image)          | Wanneeikel Free Press     |

## How to Use This Repository

### Branches

- **`main`** — Skeleton code. Relevant parts are missing or contain intentional bugs. This is the starting point for course participants.
- **`solution`** — Complete, working solutions for all tasks.

Start on `main` and work through each project. If you get stuck, the `solution` branch shows one correct implementation.

### Per-Project Workflow

Each project directory contains:
- A `README.md` describing the scenario, the task, how to run the project, and how to verify your solution
- A Maven-based Quarkus project runnable with `./mvnw quarkus:dev`

Work through the projects in order — the directory numbering reflects the course sequence. Read each project's `README.md` first; it sets the scene and describes what you need to do.

> **Note:** `01-setup/` has no code — you create the project from scratch as part of the exercise, so it has no Maven wrapper.

### Prerequisites

- Java 21+
- Maven 3.9+ (or use the included Maven wrapper)
- Docker (for dev services, e.g., databases)
- Quarkus CLI (optional but recommended): https://quarkus.io/guides/cli-tooling

All projects target **Quarkus 3.36.1** on **Java 21**.

## Running a Project

```bash
cd <project-directory>
./mvnw quarkus:dev
```

Running tests:

```bash
./mvnw test
```

In dev mode, press `r` to re-run tests on demand and `d` to open the Dev UI at http://localhost:8080/q/dev/.
