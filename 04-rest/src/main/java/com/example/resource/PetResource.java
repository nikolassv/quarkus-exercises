package com.example.resource;

import com.example.exception.PetNotFoundException;
import com.example.model.Pet;
import com.example.service.PetRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetResource {

    @Inject
    PetRegistry registry;

    @Context
    UriInfo uriInfo;

    @GET
    public List<Pet> list() {
        return registry.all();
    }

    // BUG Task 1: The API is sending the wrong HTTP status code for a successful creation.
    @POST
    public Response register(Pet pet) {
        Pet created = registry.register(pet);
        return Response.created(URI.create("/pets/" + created.id)).build();
    }

    // BUG Task 2: The id parameter never contains the value from the URL.
    @GET
    @Path("/{id}")
    public Pet getById(@PathParam("id") String id) {
        return registry.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
    }

    // TODO Task 7: Clients sending Accept: text/plain currently receive 406 Not Acceptable.
    //              Add a plain-text variant of the pet profile endpoint.
}
