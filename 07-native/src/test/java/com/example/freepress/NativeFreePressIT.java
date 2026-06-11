package com.example.freepress;

import io.quarkus.test.junit.QuarkusIntegrationTest;

/**
 * Runs the whole {@link FreePressTest} suite against the compiled native binary.
 *
 * <p>Build and run it with:
 * <pre>./mvnw verify -Dnative</pre>
 *
 * <p>On the unmodified code, three of the inherited tests fail — one per task. They all pass in
 * JVM mode, so the difference you are looking at is entirely a property of native compilation.
 */
@QuarkusIntegrationTest
class NativeFreePressIT extends FreePressTest {
}
