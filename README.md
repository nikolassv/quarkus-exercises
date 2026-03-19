# Quarkus Academy Examples

A collection of small, self-contained Quarkus projects for use in a developer course. Each project demonstrates a specific Quarkus feature or concept and includes practical tasks for participants to implement or fix.

## Topics

The following topics are covered (subject to change):

| Directory            | Topic                                              |
|----------------------|----------------------------------------------------|
| `01-setup/`          | Setting up a basic Quarkus project                 |
| `02-cdi/`            | Contexts and Dependency Injection (CDI)            |
| `03-config-logging/` | Configuration with MicroProfile Config and Logging |
| `04-rest/`           | Building REST APIs                                 |
| `05-persistence/`    | Persistence with Hibernate ORM and Panache         |
| `06-reactive/`       | Reactive programming                               |
| `07-security/`       | Security                                           |
| `08-ai/`             | Using AI with LangChain4j                          |

## How to Use This Repository

### Branches

- **`main`** — Skeleton code. Relevant parts are missing or contain intentional bugs. This is the starting point for course participants.
- **`solution`** — Complete, working solutions for all tasks.

Start on `main` and work through each project. If you get stuck, the `solution` branch shows one correct implementation.

### Per-Project Workflow

Each project directory contains:
- A `README.md` describing the task, how to run the project, and how to verify your solution
- A Maven-based Quarkus project runnable with `./mvnw quarkus:dev`

### Prerequisites

- Java 21+
- Maven 3.9+ (or use the included Maven wrapper)
- Docker (for dev services, e.g., databases)
- Quarkus CLI (optional but recommended): https://quarkus.io/guides/cli-tooling

## Running a Project

```bash
cd <project-directory>
./mvnw quarkus:dev
```

Running tests:

```bash
./mvnw test
```
