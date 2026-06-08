package com.example.uhoh.chat;

import com.example.uhoh.model.Presence;
import com.example.uhoh.model.PresenceKind;
import com.example.uhoh.model.RawPresence;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.time.Instant;

@Path("/presence")
public class PresenceResource {

    @Inject
    ChatBroadcaster broadcaster;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response send(RawPresence raw) {
        broadcaster.publishPresence(raw);
        return Response.accepted().build();
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<Presence> stream() {
        Multi<RawPresence> currentlyOnline = Multi.createFrom().iterable(broadcaster.currentlyOnline())
                .onItem().transform(sender -> new RawPresence(sender, PresenceKind.JOINED.name()));

        return Multi.createBy().concatenating().streams(currentlyOnline, broadcaster.rawPresence())
                .skip().where(rp -> rp == null
                        || rp.sender() == null
                        || rp.sender().isBlank()
                        || rp.kind() == null
                )
                .onItem().transform(rp -> new Presence(rp.sender(), getPresenceKindFromString(rp.kind()), Instant.now()))
                .skip().where(p -> p.kind() == null)
                .log();
    }

    private PresenceKind getPresenceKindFromString(String presenceKind) {
        try {
            return PresenceKind.valueOf(presenceKind);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
