package com.example.freepress.web;

import com.example.freepress.press.Archive;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * The archive page: a list of citations for every back issue the commune ever printed.
 */
@Path("/archive")
@Produces(MediaType.APPLICATION_JSON)
public class ArchiveResource {

    @Inject
    Archive archive;

    @GET
    public List<String> citations() {
        return archive.citations();
    }
}
