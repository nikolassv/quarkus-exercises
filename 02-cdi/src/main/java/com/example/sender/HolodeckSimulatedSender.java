package com.example.sender;

import com.example.model.StarfleetMessage;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;

// This sender simulates transmissions locally — useful during development so no real
// signals are sent. It should only be active in the "dev" build profile.
@ApplicationScoped
@IfBuildProfile("dev")
public class HolodeckSimulatedSender implements MessageSender {

    @Override
    public String channelName() {
        return "Holodeck Simulator";
    }

    @Override
    public void send(StarfleetMessage message) {
        System.out.printf("[HOLODECK] Simulating transmission to %s: %s%n",
                message.recipient(), message.content());
    }
}
