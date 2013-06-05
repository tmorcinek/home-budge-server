package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.Record;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.exceptions.DataValidationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/record")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class RecordResource {

    @Inject
    private EntityManager entityManager;

    @GET
    @Path("{recordId}")
    public Response getRecord(@PathParam("recordId") long recordId) {
        Record record = entityManager.find(Record.class, recordId);
        return ResponseFactory.createOkResponse(record);
    }

    @GET
    public Response getRecords(@QueryParam("accountId") long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        entityManager.refresh(account);
        return ResponseFactory.createOkResponse(account.getRecords());
    }

    @PUT
    public Response createRecord(@QueryParam("accountId") long accountId, Record record) {
        Account account = entityManager.find(Account.class, accountId);
        account.addRecord(record);
        record.setCreator(entityManager.find(User.class, record.getCreator().getId()));
        record.setPayer(entityManager.find(User.class, record.getPayer().getId()));
        List<User> users = new ArrayList<User>();
        for (User user : record.getUsers()) {
            users.add(entityManager.find(User.class, user.getId()));
        }
        record.setUsers(users);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            entityManager.persist(record);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        return ResponseFactory.createCreatedResponse(record);
    }

    @POST
    public Response updateRecord(Record updatedRecord) {
        Record record;
        try {
            validateRecord(updatedRecord);
            record = entityManager.find(Record.class, updatedRecord.getId());
            updateRecord(updatedRecord, record);
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            entityManager.merge(record);
            entityManager.refresh(record);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        return ResponseFactory.createOkResponse(record);
    }

    private void updateRecord(Record updatedRecord, Record record) {
        if (updatedRecord.getTitle() != null) {
            record.setTitle(updatedRecord.getTitle());
        }
        if (updatedRecord.getDescription() != null) {
            record.setDescription(updatedRecord.getDescription());
        }
        if (updatedRecord.getAmount() != null) {
            record.setAmount(updatedRecord.getAmount());
        }
//        if (updatedRecord.getUsers() != null && !updatedRecord.getUsers().isEmpty()){
//            List<User> users = new ArrayList<User>();
//            for (User user : updatedRecord.getUsers()) {
//                users.add(entityManager.find(User.class, user.getId()));
//            }
//            record.setUsers(users);
//        }
    }

    private void validateRecord(Record record) throws DataValidationException {
        if (record.getId() == null) {
            throw new DataValidationException("Missing record id.");
        }
    }

}
