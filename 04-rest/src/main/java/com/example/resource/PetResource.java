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

    @POST
    public Response register(Pet pet) {
        Pet created = registry.register(pet);
        return Response.created(URI.create("/pets/" + created.id)).build();
    }

    @GET
    @Path("/{id}")
    public Pet getById(@PathParam("id") String id) {
        return registry.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getByIdAsPlaintext(@PathParam("id") String id) {
        return registry.findById(id)
                .map(PetResource::petToDisplayString)
                .orElseThrow(() -> new PetNotFoundException(id));
    }

    // TODO Task 7: Clients sending Accept: text/plain currently receive 406 Not Acceptable.
    //              Add a plain-text variant of the pet profile endpoint.

    private static String petToDisplayString(Pet pet) {
        return """
                Name: %s
                Owner: %s
                Species: %s
                Bio: %s
                """.formatted(pet.name, pet.ownerHandle, pet.species, pet.bio);
    }
}
