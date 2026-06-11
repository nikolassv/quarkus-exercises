package com.example.freepress.model;

import java.time.Instant;

/**
 * A reader's comment on an article.
 */
public record Comment(String author, String text, Instant postedAt) {
}
