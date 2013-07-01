package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.exceptions.DataValidationException;
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
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/21/13
 * Time: 1:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/account")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AccountResource {

    @Inject
    private EntityManager entityManager;

    @Inject
    private SessionManager sessionManager;


//    @GET
//    @Path("/users")
//    public Response getAccountUsers(@QueryParam("accountId") long accountId) {
//        Account account = entityManager.find(Account.class, accountId);
//        entityManager.refresh(account);
//        return ResponseFactory.createOkResponse(account.getUsers());
//    }

    @GET
    @Path("{accountId}")
    public Response getAccount(@Context HttpServletRequest request, @PathParam("accountId") long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        entityManager.refresh(account);
        return ResponseFactory.createOkResponse(account);
    }

    @GET
    public Response getUserAccounts(@Context HttpServletRequest request) {
        long userId = sessionManager.getUserIdFromRequest(request);
        User user = entityManager.find(User.class, userId);
        List<Account> accounts = new ArrayList<>(0);
        if (user != null) {
            entityManager.refresh(user);
            accounts = user.getAccounts();
        }
        return ResponseFactory.createOkResponse(accounts);
    }

    @PUT
    @Path("/create")
    public Response createAccount(@Context HttpServletRequest request, Account account) {
        long userId = sessionManager.getUserIdFromRequest(request);
        return createAccount(userId, account);
    }

    @PUT
    public Response createAccount(@QueryParam("userId") long userId, Account account) {
        try {
            validateAccount(account);
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            User admin = entityManager.find(User.class, userId);
            account.addUser(admin);
            entityManager.persist(account);
            tx.commit();
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        entityManager.refresh(account);
        return ResponseFactory.createCreatedResponse(account);
    }

    private void validateAccount(Account account) throws DataValidationException {
        if (account.getName() == null || account.getName().trim().equals("")) {
            throw new DataValidationException("Account name is invalid.");
        }
    }


    @POST
    public Response addUserToAccount(@QueryParam("accountId") long accountId, @QueryParam("userId") long userId) {
        Account account = entityManager.find(Account.class, accountId);
        User user = entityManager.find(User.class, userId);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            account.addUser(user);
            entityManager.merge(account);
            tx.commit();
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        return ResponseFactory.createOkResponse(account);
    }

    @POST
    @Path("/users")
    public Response addUserToAccount(@QueryParam("accountId") long accountId, List<User> users) {
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
        return ResponseFactory.createOkResponse(account);
    }

    @DELETE
    public Response removeUserFromAccount(@QueryParam("userId") long userId, @QueryParam("accountId") long accountId) {
        // FIXME add validation to block deleting user with records.
        Account account = entityManager.find(Account.class, accountId);
        User user = entityManager.find(User.class, userId);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            account.getUsers().remove(user);
            entityManager.merge(account);
            tx.commit();
        } catch (Exception e) {
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        return ResponseFactory.createOkResponse(account);
    }


}
