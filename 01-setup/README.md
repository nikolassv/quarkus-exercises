# 01 — Setting Up a Quarkus Project

## Goal

Create a new Quarkus project from scratch, explore its structure, and get familiar with the development workflow.

## Task

### 1. Create the project

Use one of the following methods to create a new Quarkus project:

**Quarkus CLI:**
```bash
quarkus create app com.example:hello-quarkus --extension=quarkus-rest
```

**Maven plugin:**
```bash
mvn io.quarkus.platform:quarkus-maven-plugin:3.28.1:create \
  -DprojectGroupId=com.example \
  -DprojectArtifactId=hello-quarkus \
  -Dextensions=quarkus-rest
```

**Web UI:** Visit [code.quarkus.io](https://code.quarkus.io), set the group to `com.example` and the artifact to `hello-quarkus`, add the `Quarkus REST` extension, and download the generated project.

### 2. Explore the project structure

Get familiar with the generated files:

- `src/main/java` — application source code
- `src/main/resources/application.properties` — application configuration
- `src/test/java` — tests
- `pom.xml` — project descriptor and dependencies
- `mvnw` / `mvnw.cmd` — Maven wrapper (no local Maven installation required)

### 3. Add a REST endpoint

Create a resource class that exposes a `GET /hello` endpoint returning a plain text greeting, for example:

```
Hello from ti&m!
```

### 4. Run in dev mode

Start the application in dev mode:

```bash
./mvnw quarkus:dev
```

Verify your endpoint:

```bash
curl http://localhost:8080/hello
```

Try changing the greeting message while the application is running and observe live reload in action.

### 5. Run the tests

```bash
./mvnw test
```

Make sure all tests pass. Adjust the generated test if needed to match your endpoint's response.

### 6. Build and run the JVM package

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

Verify the endpoint still works after the build.

## Verification Checklist

- [ ] Project created with group ID `com.example` and artifact ID `hello-quarkus`
- [ ] `GET /hello` returns a greeting string
- [ ] Application runs in dev mode
- [ ] All tests pass
- [ ] Application runs as a built JVM JAR

<details>
<summary>Hints</summary>

- Annotate your resource class with `@Path("/hello")` and your method with `@GET` and `@Produces(MediaType.TEXT_PLAIN)`.
- The generated project may already contain an example resource class — you can use it as a starting point.
- Dev mode console shows the Quarkus Dev UI at `http://localhost:8080/q/dev/` — worth a look.

</details>
