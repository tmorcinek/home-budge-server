package com.morcinek.server.webservice.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/13/13
 * Time: 12:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserNotFoundException extends WebApplicationException {

    public UserNotFoundException() {
        super(Response.Status.NOT_FOUND);
    }
}
