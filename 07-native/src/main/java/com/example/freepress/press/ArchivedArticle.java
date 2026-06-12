package com.example.freepress.press;

import com.example.freepress.model.Section;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * A back-issue article as it is stored in the bound archive. This type only ever exists
 * inside {@link Archive}: it is produced by Jackson when the archive JSON is parsed, and it
 * is turned into a plain citation string before anything leaves the application. It never
 * appears in a REST method signature.
 */
@RegisterForReflection
public record ArchivedArticle(int year, String title, String author, Section section) {
}
