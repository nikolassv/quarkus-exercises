package com.example.freepress.press;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.time.Instant;

/**
 * The front desk notes the moment the office actually opened for this run — captured when the
 * application starts up, every time it starts up.
 */
@ApplicationScoped
public class PressDesk {

    private volatile Instant openedAt;

    void onStart(@Observes StartupEvent event) {
        openedAt = Instant.now();
    }

    public Instant openedAt() {
        return openedAt;
    }
}
