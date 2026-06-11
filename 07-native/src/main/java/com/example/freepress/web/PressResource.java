package com.example.freepress.web;

import com.example.freepress.press.PressDesk;
import com.example.freepress.press.PressRoom;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.Duration;
import java.time.Instant;

/**
 * The masthead endpoint. Reports when this run of the paper went to press, when the office
 * opened for this run, and the slogan drawn for the day.
 */
@Path("/press")
@Produces(MediaType.APPLICATION_JSON)
public class PressResource {

    @Inject
    PressDesk pressDesk;

    @GET
    public PressStatus status() {
        Instant wentToPress = PressRoom.WENT_TO_PRESS;
        Instant officeOpened = pressDesk.openedAt();
        long driftSeconds = Math.abs(Duration.between(wentToPress, officeOpened).getSeconds());
        return new PressStatus(wentToPress, officeOpened, driftSeconds, PressRoom.QUOTE_OF_THE_DAY);
    }
}
