package com.example.service;

import com.example.model.Channel;
import com.example.model.StarfleetMessage;

public interface CommunicationService {

    /**
     * Transmits a message via the specified channel.
     */
    void transmit(StarfleetMessage message, Channel channel);

    /**
     * Broadcasts a message over all available channels.
     */
    void broadcast(StarfleetMessage message);
}
