package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.Author;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/21/13
 * Time: 1:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/author")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AuthorResource {

    @Inject
    private EntityManager entityManager;

    private List<Author> authors = new ArrayList<Author>();

    public AuthorResource() {
        authors.add(new Author("Tomasz", "Morcinek", "tomasz.morcinek@gmail.com", "tmorcinek.wordpress.com"));
    }

    @GET
    public List<Author> getAuthors() {
        return authors;
    }

    @GET
    @Path("{id}")
    public Response getAuthor(@PathParam("id") int id) {
        try {
            return Response.status(Response.Status.CREATED).entity(authors.get(id - 1)).build();
        } catch (IndexOutOfBoundsException e) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

}
