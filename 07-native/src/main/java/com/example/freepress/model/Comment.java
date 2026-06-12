package com.example.freepress.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.Instant;

/**
 * A reader's comment on an article.
 */
@RegisterForReflection
public record Comment(String author, String text, Instant postedAt) {
}
