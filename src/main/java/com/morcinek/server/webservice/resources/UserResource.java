package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.database.UserManager;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.util.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 9:56 PM
 */
@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserResource {

    @Inject
    private UserManager userManager;

    @Inject
    private SessionManager sessionManager;

    @GET
    @Path("{userId}")
    public Response getUserByPathId(@PathParam("userId") long id) {
        User user = userManager.getUser(id);
        if (user == null) {
            return ResponseFactory.createBadRequestResponse("There is no user with id = " + id);
        }
        return ResponseFactory.createOkResponse(new User(user.getId(), user.getEmail(), user.getName()));
    }

    @GET
    @Path("/search")
    public Response searchUser(@QueryParam("email") String email, @QueryParam("name") String name) {
        if (email != null) {
            return getUserByEmail(email);
        }
        if (name != null) {
            return getUsersByName(name);
        }
        return ResponseFactory.createBadRequestResponse("No parameters 'email' or 'name'.");
    }

    public Response getUserByEmail(String email) {
        User user = userManager.getUserByEmail(email);
        if (user == null) {
            return ResponseFactory.createOkResponse(null);
        }
        return ResponseFactory.createOkResponse(new User(user.getId(), user.getEmail(), user.getName()));
    }

    public Response getUsersByName(String name) {
        List<User> users = userManager.getUsersByName(name);
        if (users == null) {
            return ResponseFactory.createOkResponse(null);
        }
        return ResponseFactory.createOkResponse(users);
    }

    @POST
    @Path("/me")
    public Response registerUser(@Context HttpServletRequest request, User user) {
        Long userIdFromToken = sessionManager.getUserIdFromRequest(request);
        if (userIdFromToken == null) {
            return ResponseFactory.createForbiddenResponse("You are not allowed to register another user.");
        }
        try {
            userManager.updateUser(userIdFromToken, user.getEmail(), user.getName());
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse("Creating user error.", e.getMessage());
        }
        return ResponseFactory.createOkResponse(userManager.getUser(userIdFromToken));
    }

    @PUT
    @Path("/me")
    public Response updateUser(@Context HttpServletRequest request, User user) {
        Long userIdFromToken = sessionManager.getUserIdFromRequest(request);
        if ("".equals(user.getName())) {
            return ResponseFactory.createForbiddenResponse("User name cannot be empty.");
        }
        try {
            user.setId(userIdFromToken);
            user = userManager.updateUser(user);
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse("Creating user error.", e.getMessage());
        }
        return ResponseFactory.createOkResponse(user);
    }

}
