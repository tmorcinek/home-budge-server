package com.morcinek.server.webservice.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: tomaszmorcinek
 * Date: 11.08.2013
 * Time: 00:34
 */
@Singleton
public class PermissionManager {

    @Inject
    private SessionManager sessionManager;

    public boolean validatePermision(long userId, Account account) {
        for (User user : account.getUsers()) {
            if (user.getId() == userId) {
                return true;
            }
        }
        return false;
    }
}
