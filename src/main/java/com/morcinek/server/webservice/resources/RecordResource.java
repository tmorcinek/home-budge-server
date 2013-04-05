package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.Record;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.exceptions.MethodNotImplementedException;
import com.morcinek.server.webservice.exceptions.UserLoginException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/record")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RecordResource {

    @Inject
    private EntityManager entityManager;

    @GET
    @Path("{recordId}")
    public Record getRecord(@PathParam("recordId") String recordId) {
        return entityManager.find(Record.class, recordId);
    }

    @GET
    public List<Record> getRecords(@QueryParam("accountId") long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        return account.getRecords();
    }

    @POST
    public Response createRecord(@QueryParam("accountId") long accountId, Record record) {
        Account account = entityManager.find(Account.class, record.getAccount().getId());
        account.addRecord(record);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            entityManager.persist(record);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserLoginException();
        }
        return Response.status(Response.Status.CREATED.getStatusCode()).entity(record).build();
    }

    @PUT
    public Response updateRecord(Record record) {
        Account account = entityManager.find(Account.class, record.getAccount().getId());
        account.addRecord(record);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            entityManager.merge(record);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserLoginException();
        }
        return Response.status(Response.Status.CREATED.getStatusCode()).entity(record).build();
    }

}
