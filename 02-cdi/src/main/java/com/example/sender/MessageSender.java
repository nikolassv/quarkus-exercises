package com.example.sender;

import com.example.model.StarfleetMessage;

public interface MessageSender {

    void send(StarfleetMessage message);

    String channelName();
}
