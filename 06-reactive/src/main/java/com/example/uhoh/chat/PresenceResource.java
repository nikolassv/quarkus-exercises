package com.example.uhoh.chat;

import com.example.uhoh.model.Presence;
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
        // TODO (Task 2): Compose a Mutiny pipeline that consumes broadcaster.rawPresence()
        // and emits the enriched Presence values described in the README.
        return Multi.createFrom().nothing();
    }
}
