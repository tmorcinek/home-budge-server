package com.morcinek.server.webservice.util.facebook;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morcinek.server.database.UserManager;
import com.morcinek.server.webservice.util.SessionManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 7/20/13
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class FakeFacebookSessionManager implements SessionManager {

    private long userId;

    @Override
    public boolean validateToken(String accessToken) {
        try {
            userId = Long.parseLong(accessToken);
        } catch (NumberFormatException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Long getUserIdFromToken(String accessToken) {
        return userId;
    }

    @Override
    public Long getUserIdFromRequest(HttpServletRequest request) {
        return userId;
    }
}
