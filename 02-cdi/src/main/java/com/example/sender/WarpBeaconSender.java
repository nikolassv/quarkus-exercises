package com.example.sender;

import com.example.model.StarfleetMessage;
import jakarta.enterprise.context.ApplicationScoped;

// TODO: Create a qualifier annotation for this sender and apply it here.
//       See SubspaceSender for context.
@ApplicationScoped
public class WarpBeaconSender implements MessageSender {

    @Override
    public String channelName() {
        return "Warp Beacon";
    }

    @Override
    public void send(StarfleetMessage message) {
        System.out.printf("[WARP BEACON] To: %s | Priority: %s | %s%n",
                message.recipient(), message.priority(), message.content());
    }
}
