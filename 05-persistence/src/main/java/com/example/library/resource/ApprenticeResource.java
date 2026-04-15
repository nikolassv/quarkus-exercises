package com.example.library.resource;

import com.example.library.model.Apprentice;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/apprentices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApprenticeResource {

    // Task 4
    @POST
    @Transactional
    public Response enroll(Apprentice apprentice) {
        apprentice.persist();
        return Response.status(201).entity(apprentice).build();
    }

    @GET
    public List<Apprentice> listAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        // TODO Task 7
        return Apprentice.findAll().page(page, size).list();
    }

    @GET
    @Path("/house/{house}")
    public List<Apprentice> byHouse(@PathParam("house") String house) {
        return Apprentice.findByHouse(house);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response dismiss(@PathParam("id") Long id) {
        boolean deleted = Apprentice.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(404).build();
    }
}
