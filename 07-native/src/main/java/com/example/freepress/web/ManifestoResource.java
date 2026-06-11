package com.example.freepress.web;

import com.example.freepress.press.Manifesto;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Serves the founding manifesto as plain text.
 */
@Path("/manifesto")
public class ManifestoResource {

    @Inject
    Manifesto manifesto;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String read() {
        return manifesto.text();
    }
}
