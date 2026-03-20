package com.example.resource;

import com.example.model.Species;
import com.example.model.Squeak;
import com.example.service.PetRegistry;
import com.example.service.SqueakBoard;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/squeaks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SqueakResource {

    @Inject
    SqueakBoard board;

    @Inject
    PetRegistry registry;

    // BUG Task 3: The species filter is ignored — fix it.
    @GET
    public List<Squeak> search(@QueryParam("species") Species species) {
        if (species == null) {
            return board.all();
        }
        return registry.findBySpecies(species).stream()
                .flatMap(pet -> board.findByPetId(pet.id).stream())
                .toList();
    }

    // BUG Task 4: This endpoint ignores everything the client sends — it has no parameters.
    @POST
    public Response post() {
        String petId = ""; // This should be set by the "X-Pet-Id" header
        if (petId == null || petId.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required header: X-Pet-Id")
                    .build();
        }
        Squeak created = board.post(petId, ""); // The content must be set from the request body
        URI location = URI.create("/squeaks/" + created.id);
        return Response.created(location).entity(created).build();
    }

    // TODO Task 5: This endpoint should return squeaks for the pet identified by the pet_session cookie.
    //              Currently always returns an empty list.
    @GET
    @Path("/my")
    public List<Squeak> myFeed() {
        return List.of();
    }
}
