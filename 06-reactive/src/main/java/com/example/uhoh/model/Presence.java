package com.example.uhoh.model;

import java.time.Instant;

public record Presence(String sender, PresenceKind kind, Instant at) {
}
