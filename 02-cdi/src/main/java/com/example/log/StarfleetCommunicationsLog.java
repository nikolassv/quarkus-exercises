package com.example.log;

import com.example.model.TransmissionSentEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class StarfleetCommunicationsLog {

    private static final Logger LOG = Logger.getLogger(StarfleetCommunicationsLog.class);

    private final AtomicLong transmissionCount = new AtomicLong(0);

    void onTransmission(@Observes TransmissionSentEvent event) {
        long count = transmissionCount.incrementAndGet();
        LOG.infof("[COMMS LOG] Transmission #%d via %s to %s",
                count, event.channelName(), event.message().recipient());
    }

    public long getTransmissionCount() {
        return transmissionCount.get();
    }
}
