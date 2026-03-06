package com.example;

import com.example.log.StarfleetCommunicationsLog;
import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import com.example.model.StarfleetMessage.Priority;
import com.example.sender.SubspaceSender;
import com.example.sender.WarpBeaconSender;
import com.example.service.CommunicationService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class StarfleetCommunicationServiceTest {

    @Inject
    CommunicationService service;

    @InjectMock
    SubspaceSender subspaceSender;

    @InjectMock
    WarpBeaconSender warpBeaconSender;

    @Inject
    StarfleetCommunicationsLog communicationsLog;

    // -------------------------------------------------------------------------
    // Basic transmission routing
    // -------------------------------------------------------------------------

    @Test
    void transmitViaSubspaceCallsSubspaceSender() {
        var message = new StarfleetMessage("USS Voyager", "Requesting assistance", Priority.PRIORITY);

        service.transmit(message, Channel.SUBSPACE);

        verify(subspaceSender).send(any(StarfleetMessage.class));
        verifyNoInteractions(warpBeaconSender);
    }

    @Test
    void transmitViaWarpBeaconCallsWarpBeaconSender() {
        var message = new StarfleetMessage("Deep Space 9", "Scheduled status report", Priority.ROUTINE);

        service.transmit(message, Channel.WARP_BEACON);

        verify(warpBeaconSender).send(any(StarfleetMessage.class));
        verifyNoInteractions(subspaceSender);
    }

    // -------------------------------------------------------------------------
    // Broadcast
    // -------------------------------------------------------------------------

    @Test
    void broadcastSendsToAllChannels() {
        var message = new StarfleetMessage("All Vessels", "Red alert", Priority.DISTRESS);

        service.broadcast(message);

        verify(subspaceSender).send(any(StarfleetMessage.class));
        verify(warpBeaconSender).send(any(StarfleetMessage.class));
    }

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
