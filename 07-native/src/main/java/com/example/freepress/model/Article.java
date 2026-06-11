package com.example.freepress.model;

/**
 * An article in the current issue of the magazine.
 */
public record Article(int id, String title, String author, Section section, String body) {
}
