# CLAUDE.md — Quarkus Academy Examples

Instructions for Claude when working on this project.

## Git Commits

- NEVER add a `Co-Authored-By` line to commit messages.

## Project Purpose

This repository is course material for a Quarkus developer course. It contains small, self-contained Quarkus projects, one per topic. Each project has tasks for participants to implement or fix.

## Branch Strategy

- **`main`** — skeleton/buggy code for participants. Always create new work here first.
- **`solution`** — complete solutions. After finishing work on `main`, merge `main` into `solution`, then add a solution commit on top.

Never commit solution code to `main`. Never commit skeleton/buggy code to `solution` without following it with a solution commit.

## Project Structure

```
quarkus-academy-examples/
├── README.md
├── CLAUDE.md
├── 01-setup/
├── 02-cdi/
├── 03-configuration/
├── 04-logging/
├── 05-rest/
├── 06-persistence/
├── 07-reactive/
├── 08-security/
└── 09-ai/
```

Directory numbering reflects course order. The list of topics is a draft and may change.

## Conventions for Each Project

- Each project directory is a self-contained Maven-based Quarkus project.
- Each project has its own `README.md` with: what is demonstrated, the task description, run instructions, and verification steps.
- Use `// TODO` comments to mark code participants must implement.
- Use failing tests to signal bugs participants must fix.
- Keep solution-only changes minimal and localized to the files participants are expected to touch. This reduces merge conflicts when syncing branches.
- Hints may be included in the project README inside a `<details>` block.

## Quarkus Version

Use a consistent Quarkus version across all projects. Check existing projects before creating a new one and match their version.

## Tasks vs. Implementation

- On `main`: provide the structure, dependencies, and tests. Leave the implementation incomplete or broken.
- On `solution`: provide a clean, idiomatic implementation that passes all tests.

## Topics (Draft)

1. Setting up a basic Quarkus project
2. CDI (Contexts and Dependency Injection)
3. Configuration (MicroProfile Config)
4. Logging
5. REST
6. Persistence (Hibernate ORM + Panache)
7. Reactive programming
8. Security
9. AI with LangChain4j
