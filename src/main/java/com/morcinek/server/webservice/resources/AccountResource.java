package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.exceptions.MethodNotImplementedException;
import com.morcinek.server.webservice.exceptions.UserLoginException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnitUtil;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/21/13
 * Time: 1:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/account")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AccountResource {

    @Inject
    private EntityManager entityManager;

    @GET
    public List<User> getAccountUsers(@QueryParam("accountId") long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        entityManager.detach(account);
        return account.getUsers();
    }

    @PUT
    public Response createAccount(@QueryParam("userId") long userId, Account account) {
        validateAccount(account);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            User admin = entityManager.find(User.class, userId);
            account.addUser(admin);
            entityManager.persist(account);
            entityManager.flush();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserLoginException();
        }
        return Response.status(Response.Status.CREATED.getStatusCode()).entity(account).build();
    }

    private void validateAccount(Account account) {
        if (account.getName() == null || account.getName().trim().equals("")) {
            throw new UserLoginException();
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
            entityManager.flush();
            tx.commit();
        } catch (Exception e) {
            throw new UserLoginException();
        }
        return Response.status(Response.Status.OK.getStatusCode()).entity(account).build();
    }

    @DELETE
    public Response removeUserFromAccount(@QueryParam("userId") long userId, @QueryParam("accountId") long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        User user = entityManager.find(User.class, userId);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            account.getUsers().remove(user);
            entityManager.merge(account);
            tx.commit();
        } catch (Exception e) {
            throw new UserLoginException();
        }
        return Response.status(Response.Status.OK.getStatusCode()).entity(account).build();
    }


}
