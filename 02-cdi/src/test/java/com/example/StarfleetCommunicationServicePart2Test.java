package com.example;

import com.example.log.StarfleetCommunicationsLog;
import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import com.example.model.StarfleetMessage.Priority;
import com.example.sender.SubspaceSender;
import com.example.service.CommunicationService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
class StarfleetCommunicationServicePart2Test {

    @Inject
    CommunicationService service;

    @InjectMock
    SubspaceSender subspaceSender;

    @Inject
    StarfleetCommunicationsLog communicationsLog;


    // -------------------------------------------------------------------------
    // Events and observers
    // -------------------------------------------------------------------------

    @Test
    void transmitFiresEventAndUpdatesLog() {
        long before = communicationsLog.getTransmissionCount();
        var message = new StarfleetMessage("Starfleet Command", "Arrival ETA", Priority.ROUTINE);

        service.transmit(message, Channel.SUBSPACE);

        assertThat(communicationsLog.getTransmissionCount()).isEqualTo(before + 1);
    }

    // -------------------------------------------------------------------------
    // Deduplication
    // -------------------------------------------------------------------------

    @Test
    void duplicateTransmissionIsSentOnlyOnce() {
        var message = new StarfleetMessage("Klingon High Council", "Peace treaty confirmation", Priority.PRIORITY);

        service.transmit(message, Channel.SUBSPACE);
        service.transmit(message, Channel.SUBSPACE);

        verify(subspaceSender, times(1)).send(any(StarfleetMessage.class));
    }
}
