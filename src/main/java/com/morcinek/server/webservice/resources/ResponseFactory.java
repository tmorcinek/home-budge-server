package com.morcinek.server.webservice.resources;

import com.morcinek.server.model.WebserviceError;

import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/3/13
 * Time: 12:49 PM
 */
public class ResponseFactory {

    public static Response createOkResponse(Object object) {
        return Response.status(Response.Status.OK).entity(object).build();
    }

    public static Response createCreatedResponse(Object object) {
        return Response.status(Response.Status.CREATED).entity(object).build();
    }

    public static Response createBadRequestResponse(String message) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new WebserviceError(message)).build();
    }

    public static Response createBadRequestResponse(String title, String message) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new WebserviceError(title, message)).build();
    }

    public static Response createForbiddenResponse(String title, String message) {
        return Response.status(Response.Status.FORBIDDEN).entity(new WebserviceError(title, message)).build();
    }

    public static Response createForbiddenResponse(String message) {
        return Response.status(Response.Status.FORBIDDEN).entity(new WebserviceError("Validation Error", message)).build();
    }

    public static Response createUnauthorizedResponse(String title, String message) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(new WebserviceError(title, message)).build();
    }

}
