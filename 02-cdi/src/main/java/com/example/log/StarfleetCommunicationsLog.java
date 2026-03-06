package com.example.log;

import com.example.model.TransmissionSentEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class StarfleetCommunicationsLog {

    private static final Logger LOG = Logger.getLogger(StarfleetCommunicationsLog.class);

    private final AtomicLong transmissionCount = new AtomicLong(0);

    // TODO: This method should be called automatically whenever a TransmissionSentEvent is fired.
    //       Add the appropriate CDI annotation to the event parameter to make this an observer.
    void onTransmission(TransmissionSentEvent event) {
        long count = transmissionCount.incrementAndGet();
        LOG.infof("[COMMS LOG] Transmission #%d via %s to %s",
                count, event.channelName(), event.message().recipient());
    }

    public long getTransmissionCount() {
        return transmissionCount.get();
    }
}
