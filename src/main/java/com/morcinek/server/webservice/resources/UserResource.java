package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.database.UserManager;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.util.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.List;

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

    @GET
    @Path("{userId}")
    public Response getUserByPathId(@PathParam("userId") long id) {
        return getUserById(id);
    }

    @GET
    public Response getUserById(@QueryParam("userId") long id) {
        User user = userManager.getUser(id);

        if (user == null) {
            return ResponseFactory.createBadRequestResponse("There is no user with id = " + id);
        }
        return ResponseFactory.createOkResponse(new User(user.getId(), user.getEmail(), user.getName()));
    }

    @GET
    @Path("/email")
    public Response getUserByEmail(@QueryParam("email") String email) {
        User user = userManager.getUserByEmail(email);
        if (user == null) {
            return ResponseFactory.createOkResponse(null);
        }
        return ResponseFactory.createOkResponse(new User(user.getId(), user.getEmail(), user.getName()));
    }

    @GET
    @Path("/name")
    public Response getUsersByName(@QueryParam("name") String name) {
        List<User> users = userManager.getUsersByName(name);
        if (users == null) {
            return ResponseFactory.createOkResponse(null);
        }
        return ResponseFactory.createOkResponse(users);
    }

    @PUT
    public Response registerUser(@Context HttpServletRequest request, User user) {
        Long userIdFromToken = sessionManager.getUserIdFromRequest(request);
        if (userIdFromToken == null) {
            return ResponseFactory.createForbiddenResponse("You are not allowed to register another user.");
        }
        try {
            userManager.createUserIfNotExist(userIdFromToken, user.getName(), user.getEmail());
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse("Error while creating user.");
        }
        return ResponseFactory.createOkResponse(userManager.getUser(userIdFromToken));
    }

    @POST
    public Response updateUser(@Context HttpServletRequest request, User user) {
        Long userIdFromToken = sessionManager.getUserIdFromRequest(request);
        if (userIdFromToken != user.getId()) {
            return ResponseFactory.createForbiddenResponse("You are not allowed to modify another user.");
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
