package com.example.uhoh.chat;

import com.example.uhoh.model.RawMessage;
import com.example.uhoh.model.RawPresence;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ChatBroadcaster {

    private final BroadcastProcessor<RawMessage> messages = BroadcastProcessor.create();
    private final BroadcastProcessor<RawPresence> presence = BroadcastProcessor.create();

    // Senders that have JOINED and not yet LEFT. Maintained as presence events flow through.
    private final Set<String> online = ConcurrentHashMap.newKeySet();

    public void publishMessage(RawMessage raw) {
        messages.onNext(raw);
    }

    public Multi<RawMessage> rawMessages() {
        return messages;
    }

    public void publishPresence(RawPresence raw) {
        rememberOnlineState(raw);
        presence.onNext(raw);
    }

    public Multi<RawPresence> rawPresence() {
        return presence;
    }

    /**
     * A snapshot of the senders who are currently online (JOINED and not yet LEFT),
     * in no particular order.
     */
    public Collection<String> currentlyOnline() {
        return List.copyOf(online);
    }

    private void rememberOnlineState(RawPresence raw) {
        if (raw == null || raw.sender() == null) {
            return;
        }
        if ("JOINED".equals(raw.kind())) {
            online.add(raw.sender());
        } else if ("LEFT".equals(raw.kind())) {
            online.remove(raw.sender());
        }
    }
}
