package com.example.service;

import com.example.context.ShipContext;
import com.example.interceptor.StarfleetAudit;
import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import com.example.model.TransmissionSentEvent;
import com.example.sender.MessageSender;
import com.example.sender.Subspace;
import com.example.sender.WarpBeacon;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.time.Instant;

@ApplicationScoped
@StarfleetAudit
public class StarfleetCommunicationService implements CommunicationService {

    @Subspace
    MessageSender subspaceSender;

    @WarpBeacon
    MessageSender warpBeaconSender;

    @Any
    Instance<MessageSender> allSenders;

    @Inject
    Event<TransmissionSentEvent> transmissionSentEvent;

    @Override
    public void transmit(StarfleetMessage message, Channel channel) {
        MessageSender sender = selectSender(channel);
        sender.send(message);
        transmissionSentEvent.fire(new TransmissionSentEvent(message, channel.name(), Instant.now()));
    }

    @Override
    public void broadcast(StarfleetMessage message) {
        allSenders.stream().forEach(sender -> sender.send(message));
    }

    private MessageSender selectSender(Channel channel) {
        return switch (channel) {
            case SUBSPACE -> subspaceSender;
            case WARP_BEACON -> warpBeaconSender;
        };
    }
}
