package com.morcinek.server.webservice.util;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/3/13
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SessionManager {

    public boolean validateToken(String accessToken);

    public Long getUserIdFromToken(String accessToken);

}
