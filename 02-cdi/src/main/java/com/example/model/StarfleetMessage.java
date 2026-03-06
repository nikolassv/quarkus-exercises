package com.example.model;

public record StarfleetMessage(String recipient, String content, Priority priority) {

    public enum Priority {
        ROUTINE, PRIORITY, DISTRESS
    }
}
