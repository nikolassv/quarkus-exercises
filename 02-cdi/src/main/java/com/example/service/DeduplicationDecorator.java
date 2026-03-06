package com.example.service;

import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import jakarta.enterprise.inject.Vetoed;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// This class suppresses duplicate transmissions — if the same message is sent to the same
// recipient over the same channel more than once, subsequent attempts are dropped.
//
// TODO: Turn this into a CDI decorator so it transparently wraps CommunicationService.
//       A decorator intercepts every call to the decorated bean and can add behaviour around it.
//       Steps:
//         1. Remove @Vetoed (it currently prevents CDI from seeing this class).
//         2. Add @Decorator and @Priority(Interceptor.Priority.APPLICATION) on the class.
//         3. Annotate the delegate field with @Inject and @Delegate.
//
// @Vetoed is a temporary placeholder — remove it as part of the task.
@Vetoed
public class DeduplicationDecorator implements CommunicationService {

    private static final Logger LOG = Logger.getLogger(DeduplicationDecorator.class);

    // TODO: This field must become the CDI delegate injection point.
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
