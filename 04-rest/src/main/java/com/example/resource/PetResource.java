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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Pets", description = "Manage pet profiles on BarkSquare")
public class PetResource {

    @Inject
    PetRegistry registry;

    @Context
    UriInfo uriInfo;

    @GET
    @Operation(summary = "List all pets", description = "Returns every pet registered on BarkSquare.")
    @APIResponse(responseCode = "200", description = "The list of all registered pets")
    public List<Pet> list() {
        return registry.all();
    }

    @POST
    @Operation(summary = "Register a new pet", description = "Creates a new pet profile and returns it with the assigned ID.")
    @APIResponse(responseCode = "201", description = "Pet registered successfully")
    public Response register(Pet pet) {
        Pet created = registry.register(pet);
        return Response.created(URI.create("/pets/" + created.id)).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a pet profile", description = "Returns a pet's full profile by their unique ID.")
    @APIResponse(responseCode = "200", description = "The pet profile")
    @APIResponse(responseCode = "404", description = "No pet found with the given ID")
    public Pet getById(@PathParam("id") String id) {
        return registry.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Get a pet profile as plain text", description = "Returns a formatted profile card for text-only clients.")
    @APIResponse(responseCode = "200", description = "Plain-text profile card")
    @APIResponse(responseCode = "404", description = "No pet found with the given ID")
    public String getByIdAsPlaintext(@PathParam("id") String id) {
        return registry.findById(id)
                .map(PetResource::petToDisplayString)
                .orElseThrow(() -> new PetNotFoundException(id));
    }

    private static String petToDisplayString(Pet pet) {
        return """
                Name: %s
                Owner: %s
                Species: %s
                Bio: %s
                """.formatted(pet.name, pet.ownerHandle, pet.species, pet.bio);
    }
}
