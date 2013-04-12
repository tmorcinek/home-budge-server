package com.morcinek.server;

import com.google.inject.Inject;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.guice.GuiceJUnitRunner;
import com.morcinek.server.webservice.resources.AccountResource;
import com.morcinek.server.webservice.resources.UserResource;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/27/13
 * Time: 10:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@RunWith(GuiceJUnitRunner.class)
public class AccountEntityManagerTest {

    @Inject
    private EntityManager entityManager;

    @Inject
    private UserResource userResource;

    @Inject
    private AccountResource accountResource;

    @After
    public void close(){
        entityManager.close();
    }

    @Before
    public void initAccount(){
//        User user = new User();
//        user.setName("tomek");
//        user.setEmail("tomk@morcinek.com");
//        user.setPassword("tomek");
//        userResource.createUser(user);
//        Account account = new Account();
//        account.setName("Limanowskiego");
//        accountResource.createAccount(1, account);
    }

    @Test
    public void getAccountUsersTest() throws Exception {
        // given
        long accountId = 2;

        // when
        List<Account> accounts = entityManager.createNamedQuery("findAccountById").setParameter("id", accountId).getResultList();
        Account account1 = entityManager.find(Account.class, accountId);


        // then
        assertThat(accounts).isNotNull().hasSize(1);
        assertThat(account1).isNotNull().isEqualTo(accounts.get(0));
    }
}
