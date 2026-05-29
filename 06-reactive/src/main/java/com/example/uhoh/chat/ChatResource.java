package com.example.uhoh.chat;

import com.example.uhoh.model.Message;
import com.example.uhoh.model.RawMessage;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.time.Instant;

@Path("/messages")
public class ChatResource {

    @Inject
    ChatBroadcaster broadcaster;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response send(RawMessage raw) {
        broadcaster.publishMessage(raw);
        return Response.accepted().build();
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<Message> stream() {
        return broadcaster.rawMessages()
                .skip().where(m -> m == null
                        || m.text() == null
                        || m.text().isBlank()
                        || m.sender() == null
                        || m.sender().isBlank()
                )
                .onItem().transform(rw -> new Message(
                        rw.sender(),
                        rw.text().trim(),
                        Instant.now()
                ))
                .log();
    }
}
