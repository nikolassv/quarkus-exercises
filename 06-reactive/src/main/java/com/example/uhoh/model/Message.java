package com.example.uhoh.model;

import java.time.Instant;

public record Message(String sender, String text, Instant sentAt) {
}
