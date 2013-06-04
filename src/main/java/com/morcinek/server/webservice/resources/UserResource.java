package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.User;
import com.morcinek.server.model.WebserviceError;
import com.morcinek.server.webservice.util.SessionManager;
import com.morcinek.server.webservice.util.facebook.model.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/user")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserResource {

    @Inject
    private UserManager userManager;

    @Inject
    private SessionManager sessionManager;

    @Context
    Request request;

    @GET
    @Path("{userId}")
    public Response getUserById(@QueryParam("userId") long id) {
        User user = userManager.getUser(id);
        if (user == null) {
            return ResponseFactory.createBadRequestResponse("There is no user with id = " + id);
        }
        return ResponseFactory.createOkResponse(new User(user.getId(), user.getEmail(), user.getName()));
    }

    @POST
    public Response updateUser(@Context HttpServletRequest request, User user) {
        String accessToken = request.getHeader("accessToken");
        if (user.getId() != sessionManager.getUserIdFromToken(accessToken)) {
            return Response.status(Response.Status.FORBIDDEN).entity(new WebserviceError("You are not allowed to modify another user.")).build();
        }
        if ("".equals(user.getName())) {
            return ResponseFactory.createBadRequestResponse("User name cannot be empty.");
        }
        try {
            user = userManager.updateUser(user);
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        return ResponseFactory.createOkResponse(user);
    }

}
