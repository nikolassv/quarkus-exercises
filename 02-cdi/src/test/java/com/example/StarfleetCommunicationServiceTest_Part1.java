package com.example;

import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import com.example.model.StarfleetMessage.Priority;
import com.example.sender.Subspace;
import com.example.sender.SubspaceSender;
import com.example.sender.WarpBeacon;
import com.example.sender.WarpBeaconSender;
import com.example.service.CommunicationService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@QuarkusTest
class StarfleetCommunicationServiceTest_Part1 {

    @Inject
    CommunicationService service;

    @InjectMock @Subspace
    SubspaceSender subspaceSender;

    @InjectMock @WarpBeacon
    WarpBeaconSender warpBeaconSender;

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
}
