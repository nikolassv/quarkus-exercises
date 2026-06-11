package com.example.freepress.model;

import java.util.List;

/**
 * An article together with the reader comments it has collected. Returned by
 * {@code GET /articles/{id}}.
 */
public record ArticleView(Article article, List<Comment> comments) {
}
