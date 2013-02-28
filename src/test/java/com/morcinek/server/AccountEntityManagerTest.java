package com.morcinek.server;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.webservice.guice.GuiceJUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/27/13
 * Time: 10:29 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(GuiceJUnitRunner.class)
public class AccountEntityManagerTest {

    @Inject
    private EntityManager entityManager;

    @Test
    public void getAccountUsersTest() throws Exception {
        // given
        long accountId = 0;

        // when
        TypedQuery<Account> accountTypedQuery = entityManager.createNamedQuery("findAccountById", Account.class)
                .setParameter("id", accountId);
        List<Account> accounts = accountTypedQuery.getResultList();

        Account account1 = entityManager.find(Account.class, accountId);


        // then
        assertThat(accounts).isNotNull().hasSize(1);
        assertThat(account1).isNotNull().equals(accounts.get(0));
    }
}
