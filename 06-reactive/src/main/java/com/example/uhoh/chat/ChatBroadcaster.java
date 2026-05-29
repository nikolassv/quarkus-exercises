package com.example.uhoh.chat;

import com.example.uhoh.model.RawMessage;
import com.example.uhoh.model.RawPresence;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatBroadcaster {

    private final BroadcastProcessor<RawMessage> messages = BroadcastProcessor.create();
    private final BroadcastProcessor<RawPresence> presence = BroadcastProcessor.create();

    public void publishMessage(RawMessage raw) {
        messages.onNext(raw);
    }

    public Multi<RawMessage> rawMessages() {
        return messages;
    }

    public void publishPresence(RawPresence raw) {
        presence.onNext(raw);
    }

    public Multi<RawPresence> rawPresence() {
        return presence;
    }
}
