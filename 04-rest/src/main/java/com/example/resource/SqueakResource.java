package com.example.resource;

import com.example.model.Species;
import com.example.model.Squeak;
import com.example.service.PetRegistry;
import com.example.service.SqueakBoard;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Path("/squeaks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Squeaks", description = "Browse and post pet squeaks")
public class SqueakResource {

    @Inject
    SqueakBoard board;

    @Inject
    PetRegistry registry;

    @GET
    @Operation(summary = "Browse the squeak feed", description = "Returns all squeaks, optionally filtered by species.")
    @APIResponse(responseCode = "200", description = "The squeak feed")
    public List<Squeak> search(@QueryParam("species") Species species) {
        if (species == null) {
            return board.all();
        }
        return registry.findBySpecies(species).stream()
                .flatMap(pet -> board.findByPetId(pet.id).stream())
                .toList();
    }

    @POST
    @Operation(summary = "Post a squeak", description = "Creates a new squeak on behalf of the pet identified by the X-Pet-Id header.")
    @APIResponse(responseCode = "201", description = "Squeak posted successfully")
    @APIResponse(responseCode = "400", description = "Missing X-Pet-Id header or content exceeds 140 characters")
    public Response post(@HeaderParam("X-Pet-Id") String petId, Squeak squeak) {
        if (petId == null || petId.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required header: X-Pet-Id")
                    .build();
        }

        if (squeak.content.length() > 140) {
            throw new BadRequestException("Content too long!");
        }

        Squeak created = board.post(petId, squeak.content);
        URI location = URI.create("/squeaks/" + created.id);
        return Response.created(location).entity(created).build();
    }

    @GET
    @Path("/my")
    @Operation(summary = "My squeak feed", description = "Returns squeaks posted by the pet identified by the pet_session cookie.")
    @APIResponse(responseCode = "200", description = "The pet's squeaks, or an empty list if no session cookie is present")
    public List<Squeak> myFeed(@CookieParam("pet_session") String petId) {
        if (petId == null) return List.of();
        return board.findByPetId(petId);
    }
}
