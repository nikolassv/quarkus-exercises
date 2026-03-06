package com.example.sender;

import com.example.model.StarfleetMessage;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SubspaceSender implements MessageSender {

    @Override
    public String channelName() {
        return "Subspace Radio";
    }

    @Override
    public void send(StarfleetMessage message) {
        System.out.printf("[SUBSPACE] To: %s | Priority: %s | %s%n",
                message.recipient(), message.priority(), message.content());
    }
}
