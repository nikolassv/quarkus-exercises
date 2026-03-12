package com.example.service;

import com.example.context.ShipContext;
import com.example.interceptor.StarfleetAudit;
import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import com.example.sender.MessageSender;
import com.example.sender.Subspace;
import com.example.sender.WarpBeacon;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
@StarfleetAudit
public class StarfleetCommunicationService implements CommunicationService {

    @Subspace
    MessageSender subspaceSender;

    @WarpBeacon
    MessageSender warpBeaconSender;

    @Any
    Instance<MessageSender> allSenders;

    @Override
    public void transmit(StarfleetMessage message, Channel channel) {
        MessageSender sender = selectSender(channel);
        sender.send(message);
        // TODO: An event of type TransmissionSentEvent should be fired after every transmission
        //       so that other parts of the system (like StarfleetCommunicationsLog) can react.
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
