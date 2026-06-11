package com.example.freepress.press;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Serves the commune's founding manifesto.
 *
 * <p>The text lives in {@code src/main/resources/press/manifesto.txt} and is read straight off
 * the classpath whenever a reader asks for it.
 */
@ApplicationScoped
public class Manifesto {

    private static final String RESOURCE_PATH = "press/manifesto.txt";

    public String text() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream in = classLoader.getResourceAsStream(RESOURCE_PATH)) {
            if (in == null) {
                throw new IllegalStateException("The manifesto could not be found on the classpath: " + RESOURCE_PATH);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("The manifesto could not be read", e);
        }
    }
}
