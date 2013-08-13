package com.morcinek.server;

import com.morcinek.server.database.RecordManager;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.ModelFactory;
import com.morcinek.server.model.Record;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.guice.SimpleGuiceJUnitRunner;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/3/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SimpleGuiceJUnitRunner.class)
public class RecordManagerTest {

    @Inject
    private EntityManager entityManager;

    @Inject
    private RecordManager recordManager;
    private Account account;

    @Before
    public void setUp() throws Exception {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        User marika = ModelFactory.createUser(entityManager, 502, "Marika", "marika.mala@pl");
        User loool = ModelFactory.createUser(entityManager, 503, "loool", "nie.wiem.co.napisac@pl");
        User user = ModelFactory.createUser(entityManager, 504, "Milton Friedman", "mala@karolina.pl");
        account = ModelFactory.createAccount(entityManager, "Account", marika, loool, user);
        ModelFactory.createRecord(entityManager, account, 23.0, "Obiad", marika, marika, loool, marika);
        ModelFactory.createRecord(entityManager, account, 25.0, "Pranie", marika, marika, loool);
        ModelFactory.createRecord(entityManager, account, 17.0, "Wodka", loool, loool, loool, marika);
        ModelFactory.createRecord(entityManager, account, 15.0, "piwa", loool, loool, loool, marika, user);
        transaction.commit();
    }

    @After
    public void tearDown() throws Exception {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Query nativeQuery = entityManager.createNativeQuery("delete from User; delete from Account; " +
                "delete from Account_User; delete from Record; delete from Record_User");
        nativeQuery.executeUpdate();
        transaction.commit();
    }

    @Test
    public void getRecordsForUserFromAccountTest() throws Exception {
        // given when
        List<Record> recordsForUserFromAccount = recordManager.getRecordsForUserFromAccount(account.getId(), 504);
        // then
        Assertions.assertThat(recordsForUserFromAccount).hasSize(1);
    }

}
