package com.morcinek.server.database;

import com.morcinek.server.model.Account;
import com.morcinek.server.model.Record;
import com.morcinek.server.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomaszmorcinek
 * Date: 11.08.2013
 * Time: 00:52
 */
public class RecordManager {

    @Inject
    private EntityManager entityManager;

    public List<Record> getRecordsForUserFromAccount(long accountId, long userId) {
        Account account = entityManager.find(Account.class, accountId);
        return getRecordsForUserFromAccount(account, userId);
    }

    public List<Record> getRecordsForUserFromAccount(Account account, long userId) {
        entityManager.refresh(account);
        List<Record> records = new ArrayList<>();
        for (Record record : account.getRecords()) {
            if (recordHasUser(record, userId)) {
                records.add(record);
            }
        }
        return records;
    }

    private boolean recordHasUser(Record record, long userId) {
        if (record.getCreator().getId() == userId || record.getPayer().getId() == userId) {
            return true;
        }
        for (User user : record.getUsers()) {
            if (user.getId() == userId) {
                return true;
            }
        }
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

}
