package com.example.sender;

import com.example.model.StarfleetMessage;
import jakarta.enterprise.context.ApplicationScoped;

// TODO: Both SubspaceSender and WarpBeaconSender implement MessageSender.
//       Without a way to tell them apart, CDI cannot decide which one to inject
//       when a MessageSender is requested — resulting in an ambiguous dependency error.
//       Create a qualifier annotation for this sender and apply it here.
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
