package com.example.freepress.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

/**
 * An article together with the reader comments it has collected. Returned by
 * {@code GET /articles/{id}}.
 */
@RegisterForReflection
public record ArticleView(Article article, List<Comment> comments) {
}
