package com.example.resource;

import com.example.context.ShipContext;
import com.example.model.Channel;
import com.example.model.StarfleetMessage;
import com.example.model.StarfleetMessage.Priority;
import com.example.service.CommunicationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/transmit")
public class TransmissionResource {

    @Inject
    CommunicationService communicationService;

    @Inject
    ShipContext shipContext;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String transmit(
            @QueryParam("shipName") @DefaultValue("Unknown vessel") String shipName,
            @QueryParam("officer") @DefaultValue("Unknown officer") String officer,
            @QueryParam("recipient") @DefaultValue("Starfleet Command") String recipient,
            @QueryParam("message") @DefaultValue("No message") String message,
            @QueryParam("channel") @DefaultValue("SUBSPACE") String channelStr) {

        shipContext.setShipName(shipName);
        shipContext.setOfficerOnDuty(officer);

        Channel channel = Channel.valueOf(channelStr);
        communicationService.transmit(
                new StarfleetMessage(recipient, message, Priority.ROUTINE), channel);

        return String.format(
                "Transmission sent.%n  From:    %s (officer: %s)%n  To:      %s%n  Channel: %s%n",
                shipContext.getShipName(), shipContext.getOfficerOnDuty(), recipient, channel);
    }

    // This endpoint reads ShipContext WITHOUT setting it first.
    // With @Singleton scope: returns the ship name left over from the last request.
    // With @RequestScoped scope: always returns null — each request starts fresh.
    @GET
    @Path("/context")
    @Produces(MediaType.TEXT_PLAIN)
    public String context() {
        return String.format("Ship context for this request:%n  Ship:    %s%n  Officer: %s%n",
                shipContext.getShipName(), shipContext.getOfficerOnDuty());
    }
}
