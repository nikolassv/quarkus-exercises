package com.example.library.resource;

import com.example.library.model.Tome;
import com.example.library.repository.TomeRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/tomes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TomeResource {

    @Inject
    TomeRepository tomeRepository;

    @GET
    public List<Tome> listAll() {
        return tomeRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Tome getById(@PathParam("id") Long id) {
        return tomeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tome not found"));
    }

    @GET
    @Path("/author/{author}")
    public List<Tome> byAuthor(@PathParam("author") String author) {
        return tomeRepository.findByAuthor(author);
    }

    @POST
    @Transactional
    public Response addTome(Tome tome) {
        tomeRepository.save(tome);
        return Response.status(201).entity(tome).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response removeTome(@PathParam("id") Long id) {
        tomeRepository.delete(id);
        return Response.noContent().build();
    }
}
