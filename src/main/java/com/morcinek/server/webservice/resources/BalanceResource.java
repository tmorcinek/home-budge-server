package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.Balance;
import com.morcinek.server.model.Record;
import com.morcinek.server.model.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/balance")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class BalanceResource {

    @Inject
    private EntityManager entityManager;

    @GET
    public Response getBalances(@QueryParam("accountId") long accountId) {
        Map<User, Balance> userBalanceMap = new HashMap<User, Balance>();
        List<Record> records = getRecordsForAccount(accountId);
        for (Record record : records) {
            User payer = record.getPayer();
            double amount = record.getAmount();
            addBalanceToUser(userBalanceMap, payer, amount);
            List<User> users = record.getUsers();
            for (User user : users) {
                addBalanceToUser(userBalanceMap, user, -amount / users.size());
            }
        }
        return ResponseFactory.createOkResponse(userBalanceMap.values());
    }

    private void addBalanceToUser(Map<User, Balance> userBalanceMap, User user, double amount) {
        Balance balance = userBalanceMap.get(user);
        if (balance == null) {
            userBalanceMap.put(user, new Balance(user.getId(), amount));
        } else {
            balance.addToBalance(amount);
        }
    }

    private List<Record> getRecordsForAccount(long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        if (account == null) {
            return new ArrayList<Record>();
        }
        entityManager.refresh(account);
        return account.getRecords();
    }

    @GET
    @Path("/total")
    public Response getTotalBalance(@QueryParam("accountId") long accountId) {
        Query sumQuery = entityManager.createNativeQuery("select sum(AMOUNT) from RECORD where ACCOUNT_ID = ?");
        sumQuery.setParameter(1, accountId);
        Double result = (Double) sumQuery.getSingleResult();
        if (result == null) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(new WebserviceError("There is no records for this account.")).build();
            result = 0d;
        }
        return ResponseFactory.createOkResponse(new Balance(null, result));
    }
}
