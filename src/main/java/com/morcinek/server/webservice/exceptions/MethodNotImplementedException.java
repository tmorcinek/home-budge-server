package com.morcinek.server.webservice.exceptions;

import javax.ws.rs.WebApplicationException;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/13/13
 * Time: 2:15 AM
 * To change this template use File | Settings | File Templates.
 */
public final class MethodNotImplementedException extends WebApplicationException {

    public MethodNotImplementedException() {
        super(501);
    }
}
