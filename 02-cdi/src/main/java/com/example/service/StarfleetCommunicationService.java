package com.example.service;

import com.example.context.ShipContext;
import com.example.interceptor.StarfleetAudit;
import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import com.example.sender.MessageSender;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
@StarfleetAudit
public class StarfleetCommunicationService implements CommunicationService {

    // TODO: Both fields below are ambiguous — CDI cannot choose between SubspaceSender
    //       and WarpBeaconSender since they have no qualifiers yet.
    //       Create qualifier annotations and apply them to the sender beans and to
    //       these injection points.
    @Inject
    MessageSender subspaceSender;

    @Inject
    MessageSender warpBeaconSender;

    @Inject
    @Any
    Instance<MessageSender> allSenders;

    @Inject
    ShipContext shipContext;

    @Override
    public void transmit(StarfleetMessage message, Channel channel) {
        MessageSender sender = selectSender(channel);
        sender.send(message);
        // TODO: An event of type TransmissionSentEvent should be fired after every transmission
        //       so that other parts of the system (like StarfleetCommunicationsLog) can react.
    }

    @Override
    public void broadcast(StarfleetMessage message) {
        // TODO: Send the message to all registered MessageSender beans.
        //       Use the allSenders Instance to iterate over them and call send() on each.
    }

    private MessageSender selectSender(Channel channel) {
        return switch (channel) {
            case SUBSPACE -> subspaceSender;
            case WARP_BEACON -> warpBeaconSender;
        };
    }
}
