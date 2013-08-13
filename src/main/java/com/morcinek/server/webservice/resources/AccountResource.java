package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.database.RecordManager;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.util.PermissionManager;
import com.morcinek.server.webservice.util.SessionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/21/13
 * Time: 1:26 AM
 */
@Path("/accounts")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AccountResource {

    @Inject
    private EntityManager entityManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private RecordManager recordManager;

    @GET
    @Path("{accountId}")
    public Response getAccount(@Context HttpServletRequest request, @PathParam("accountId") long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        if (!permissionManager.validatePermision(sessionManager.getUserIdFromRequest(request), account)) {
            return ResponseFactory.createUnauthorizedResponse("Authorization Error", "User do not own this account.");
        }
        entityManager.refresh(account);
        return ResponseFactory.createOkResponse(account);
    }

    @GET
    public Response getUserAccounts(@Context HttpServletRequest request) {
        long userId = sessionManager.getUserIdFromRequest(request);
        User user = entityManager.find(User.class, userId);
        List<Account> accounts = new ArrayList<Account>(0);
        if (user != null) {
            entityManager.refresh(user);
            accounts = user.getAccounts();
        }
        return ResponseFactory.createOkResponse(accounts);
    }

    @POST
    public Response createAccount(@Context HttpServletRequest request, Account account) {
        long userId = sessionManager.getUserIdFromRequest(request);
        if (!validateAccount(account)) {
            return ResponseFactory.createForbiddenResponse("Validation Error", "Account name cannot be empty.");
        }
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            account.addUser(new User(userId));
            entityManager.persist(account);
            tx.commit();
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        entityManager.refresh(account);
        return ResponseFactory.createCreatedResponse(account);
    }

    private boolean validateAccount(Account account) {
        if (account.getName() == null || account.getName().trim().equals("")) {
            return false;
        }
        return true;
    }

    @PUT
    @Path("{accountId}/users")
    public Response addUsersToAccount(@PathParam("accountId") long accountId, List<User> users) {
        Account account = entityManager.find(Account.class, accountId);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            account.getUsers().addAll(users);
            entityManager.merge(account);
            tx.commit();
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        entityManager.refresh(account);
        return ResponseFactory.createOkResponse(account);
    }

    @DELETE
    @Path("{accountId}/users/{userId}")
    public Response removeUserFromAccount(@Context HttpServletRequest request, @PathParam("accountId") long accountId, @PathParam("userId") long userId) {
        Account account = entityManager.find(Account.class, accountId);
        if (!permissionManager.validatePermision(sessionManager.getUserIdFromRequest(request), account)) {
            return ResponseFactory.createUnauthorizedResponse("Authorization Error", "User cannot create record for this account.");
        }
        User user = entityManager.find(User.class, userId);
        if (!recordManager.getRecordsForUserFromAccount(account, userId).isEmpty()) {
            return ResponseFactory.createForbiddenResponse("Validation Error", "User cannot be removed, due to financial involvement.");
        }
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            account.getUsers().remove(user);
            entityManager.merge(account);
            tx.commit();
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        entityManager.refresh(account);
        return ResponseFactory.createOkResponse(account);
    }

}
