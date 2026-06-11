package com.example.freepress.web;

/**
 * The body of a {@code POST /articles/{id}/comments} request.
 */
public record NewComment(String author, String text) {
}
