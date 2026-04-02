package com.example.library.resource;

import com.example.library.model.Apprentice;
import com.example.library.model.Borrowing;
import com.example.library.model.Tome;
import com.example.library.repository.BorrowingRepository;
import com.example.library.repository.TomeRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/borrowings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BorrowingResource {

    @Inject
    BorrowingRepository borrowingRepository;

    @Inject
    TomeRepository tomeRepository;

    @GET
    @Path("/active")
    public List<Borrowing> activeLoans() {
        return borrowingRepository.findActiveLoans();
    }

    @POST
    @Transactional
    public Response borrow(BorrowRequest request) {
        Tome tome = tomeRepository.findById(request.tomeId)
                .orElseThrow(() -> new NotFoundException("Tome not found"));
        Apprentice apprentice = Apprentice.findById(request.apprenticeId);
        if (apprentice == null) {
            throw new NotFoundException("Apprentice not found");
        }
        Borrowing borrowing = borrowingRepository.borrow(tome, apprentice);
        return Response.status(201).entity(borrowing).build();
    }

    @PUT
    @Path("/{id}/return")
    @Transactional
    public Response returnTome(@PathParam("id") Long id) {
        borrowingRepository.returnTome(id);
        return Response.noContent().build();
    }
}
