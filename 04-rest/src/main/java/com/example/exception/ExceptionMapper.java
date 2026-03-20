package com.example.exception;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ExceptionMapper {
    @ServerExceptionMapper
    public Response mapPetNotFoundException(PetNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
