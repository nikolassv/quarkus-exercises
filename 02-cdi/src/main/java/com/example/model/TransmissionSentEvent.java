package com.example.model;

import java.time.Instant;

public record TransmissionSentEvent(StarfleetMessage message, String channelName, Instant timestamp) {
}
