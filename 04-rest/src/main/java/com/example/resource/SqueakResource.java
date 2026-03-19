package com.example.resource;

import com.example.model.Species;
import com.example.model.Squeak;
import com.example.service.PetRegistry;
import com.example.service.SqueakBoard;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
    public List<Squeak> search(@QueryParam("type") Species species) {
        if (species == null) {
            return board.all();
        }
        return registry.findBySpecies(species).stream()
                .flatMap(pet -> board.findByPetId(pet.id).stream())
                .toList();
    }

    // BUG Task 4: Every post attempt is rejected, even when the client sends the correct header.
    @POST
    public Response post(@HeaderParam("Pet-Id") String petId, Squeak squeak) {
        if (petId == null || petId.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required header: X-Pet-Id")
                    .build();
        }
        Squeak created = board.post(petId, squeak.content);
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
