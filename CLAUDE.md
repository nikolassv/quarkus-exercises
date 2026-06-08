# CLAUDE.md — Quarkus Academy Examples

Instructions for Claude when working on this project.

## Git Commits

- NEVER add a `Co-Authored-By` line to commit messages.

## Project Purpose

This repository is course material for a Quarkus developer course. It contains small, self-contained Quarkus projects, one per topic. Each project has tasks for participants to implement or fix.

## Branch Strategy

- **`main`** — skeleton/buggy code for participants. Always create new work here first.
- **`solution`** — complete solutions. After finishing work on `main`, the **user** merges `main` into `solution` and writes the solution commit manually.

Never commit solution code to `main`. Never commit skeleton/buggy code to `solution` without following it with a solution commit.

**Never write the solution yourself.** Claude's scope ends at the `main` branch (skeleton + failing tests + README). The user writes the solution commit by hand — working through the tasks themselves is how they validate that tasks are doable, well-scoped, and consistent with the course material. If Claude implements the solution, that validation step is lost. Do not produce a passing implementation and do not "make the tests green" as a verification step. Mechanical git operations (committing the skeleton, merging `main` into `solution` so the user can land their solution commit on top) are fine when the user explicitly asks.

## Project Structure

```
quarkus-academy-examples/
├── README.md
├── CLAUDE.md
├── 01-setup/
├── 02-cdi/
├── 03-configuration and logging/
├── 04-rest/
├── 05-persistence/
├── 06-reactive/
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

You have the Quarkus CLI `quarkus` at your disposal. Use it if necessary.

## Tasks vs. Implementation

- On `main`: provide the structure, dependencies, and tests. Leave the implementation incomplete or broken.
- On `solution`: provide a clean, idiomatic implementation that passes all tests.

## Writing Task Descriptions in READMEs

**Describe the symptom or desired outcome — not the solution.**
Say what is wrong and what the correct end state looks like. Let the developer figure out the fix. Do not explain which property to change, which annotation to add, or what value to use.

**Make developers feel the need for a concept before introducing it.**
Instead of presenting a broken example of a feature and asking them to fix it, design the scenario so that the developer encounters a real pain point first (e.g. duplicated values, inconsistent output). Then suggest the concept as a possible solution without spelling out the implementation.

**Never pre-write the solution inside a TODO comment or commented-out code block.**
Participants must write the code or configuration themselves. Scaffolded comments that just need to be uncommented are not a learning exercise. Remove them and let developers write it from scratch.

**Test behaviour, not raw config values.**
Failing tests should assert on meaningful application behaviour (e.g. `getStatus()` returns `"OPERATIONAL"`), not on internal implementation details like "pressureMin < pressureMax". The test failure should read like a business requirement, not a config validator.

**Do not add hints unless the task genuinely cannot be solved without one.**
The CDI example has no hints at all. Participants have just completed the relevant course chapter and should have the knowledge to figure tasks out. When in doubt, omit the hint.

## Topics (Draft)

1. Setting up a basic Quarkus project
2. CDI (Contexts and Dependency Injection)
3. Configuration (MicroProfile Config)
4. Logging
5. REST
6. Persistence (Hibernate ORM + Panache)
7. Reactive programming
