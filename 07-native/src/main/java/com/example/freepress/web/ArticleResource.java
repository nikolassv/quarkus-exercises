package com.example.freepress.web;

import com.example.freepress.Magazine;
import com.example.freepress.model.Article;
import com.example.freepress.model.ArticleView;
import com.example.freepress.model.Comment;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Reading the current issue and leaving comments. This resource works the same in JVM
 * mode and as a native binary — it is here so the site has something that simply works.
 */
@Path("/articles")
@Produces(MediaType.APPLICATION_JSON)
public class ArticleResource {

    @Inject
    Magazine magazine;

    @GET
    public List<Article> list() {
        return magazine.all();
    }

    @GET
    @Path("/{id}")
    public Response read(@PathParam("id") int id) {
        return magazine.byId(id)
                .map(article -> Response.ok(new ArticleView(article, magazine.commentsFor(id))).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response comment(@PathParam("id") int id, NewComment input) {
        if (magazine.byId(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Comment saved = magazine.addComment(id, input.author(), input.text());
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }
}
