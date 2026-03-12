package com.example.service;

import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// This class suppresses duplicate transmissions — if the same message is sent to the same
// recipient over the same channel more than once, subsequent attempts are dropped.
//
// TODO: Turn this into a CDI decorator so it transparently wraps CommunicationService.
// @Vetoed is a temporary placeholder — remove it as part of the task.
@Decorator
@Priority(Interceptor.Priority.APPLICATION)
public class DeduplicationDecorator implements CommunicationService {

    private static final Logger LOG = Logger.getLogger(DeduplicationDecorator.class);

    // TODO: This field must become the CDI delegate injection point.
    @Inject
    @Delegate
    CommunicationService delegate;

    private final Set<String> recentTransmissions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void transmit(StarfleetMessage message, Channel channel) {
        String key = channel + ":" + message.recipient() + ":" + message.content();
        if (!recentTransmissions.add(key)) {
            LOG.warnf("[DEDUP] Suppressing duplicate transmission to %s via %s",
                    message.recipient(), channel);
            return;
        }
        delegate.transmit(message, channel);
    }

    @Override
    public void broadcast(StarfleetMessage message) {
        delegate.broadcast(message);
    }
}
