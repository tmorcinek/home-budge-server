package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.exceptions.MethodNotImplementedException;

import javax.persistence.EntityManager;
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
//        Query accountById = entityManager.createNativeQuery("findAccountById", Account.class);
//        accountById.setParameter(1,"accountId");

        throw new MethodNotImplementedException();
    }

    @PUT
    public Response createAccount(@QueryParam("userId") long userId, Account account) {

        throw new MethodNotImplementedException();
    }


    @POST
    public Response addUserToAccount(@QueryParam("accountId") long accountId, @QueryParam("userId") long userId) {
//        Query accountById = entityManager.createNativeQuery("findAccountById", Account.class);
//        accountById.setParameter(1,"accountId");


        throw new MethodNotImplementedException();
    }

    @DELETE
    public Response removeUserFromAccount(@QueryParam("accountId") long accountId, @QueryParam("userId") long userId) {
//        Query accountById = entityManager.createNativeQuery("findAccountById", Account.class);
//        accountById.setParameter(1,"accountId");


        throw new MethodNotImplementedException();
    }



}
