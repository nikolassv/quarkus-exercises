package com.example.uhoh.chat;

import com.example.uhoh.model.Message;
import com.example.uhoh.model.RawMessage;
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
        // TODO (Task 1): Compose a Mutiny pipeline that consumes broadcaster.rawMessages()
        // and emits the enriched Message values described in the README.
        return Multi.createFrom().nothing();
    }
}
