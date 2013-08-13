package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.Record;
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

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 9:56 PM
 */
@Path("/records/accounts")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class RecordResource {

    @Inject
    private EntityManager entityManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private PermissionManager permissionManager;

    @GET
    @Path("{accountId}/records")
    public Response getRecords(@PathParam("accountId") long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        entityManager.refresh(account);
        return ResponseFactory.createOkResponse(account.getRecords());
    }

    @POST
    @Path("{accountId}/records")
    public Response createRecord(@Context HttpServletRequest request, @PathParam("accountId") long accountId, Record record) {
        Account account = entityManager.find(Account.class, accountId);
        long userId = sessionManager.getUserIdFromRequest(request);
        if (!permissionManager.validatePermision(userId, account)) {
            return ResponseFactory.createUnauthorizedResponse("Authorization Error", "User cannot create record for this account.");
        }
        account.addRecord(record);
        record.setCreator(new User(userId));
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            entityManager.persist(record);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        entityManager.refresh(record);
        return ResponseFactory.createCreatedResponse(record);
    }

    @PUT
    @Path("{accountId}/records/{recordId}")
    public Response updateRecord(@Context HttpServletRequest request, @PathParam("accountId") long accountId, @PathParam("recordId") long recordId, Record updatedRecord) {
        Record record;
        try {
            record = entityManager.find(Record.class, recordId);
            if (record == null) {
                return ResponseFactory.createForbiddenResponse("Validation Error", "No record with such id.");
            }
            if (record.getAccount().getId() != accountId) {
                return ResponseFactory.createForbiddenResponse("Validation Error", "Record does not exist in given account.");
            }
            long userId = sessionManager.getUserIdFromRequest(request);
            if (!permissionManager.validatePermision(userId, record.getAccount())) {
                return ResponseFactory.createUnauthorizedResponse("Authorization Error", "User cannot create record for this account.");
            }

            // updating creator to show who updated this record
            record.setCreator(entityManager.find(User.class, userId));

            updateRecord(updatedRecord, record);
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            entityManager.merge(record);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.createBadRequestResponse(e.getMessage());
        }
        entityManager.refresh(record);
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
        if (updatedRecord.getPayer() != null) {
            record.setPayer(updatedRecord.getPayer());
        }
        if (updatedRecord.getUsers() != null && !updatedRecord.getUsers().isEmpty()) {
            record.setUsers(updatedRecord.getUsers());
        }
    }

}
