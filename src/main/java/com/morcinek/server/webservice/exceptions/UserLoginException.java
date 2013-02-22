package com.morcinek.server.webservice.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 2/10/13
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserLoginException extends WebApplicationException {

    public UserLoginException() {
        this(null);
    }

    public UserLoginException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.TEXT_PLAIN).build());
    }

}
