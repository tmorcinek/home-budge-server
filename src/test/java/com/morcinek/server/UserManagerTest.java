package com.morcinek.server;

import com.morcinek.server.database.UserManager;
import com.morcinek.server.model.ModelFactory;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.guice.SimpleGuiceJUnitRunner;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/3/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SimpleGuiceJUnitRunner.class)
public class UserManagerTest {

    @Inject
    private EntityManager entityManager;

    @Inject
    private UserManager userManager;

    @Test
    public void getUserTestNoUser() throws Exception {
        // give when
        User user = userManager.getUser(210);

        // then
        Assertions.assertThat(user).isNull();
    }

    @Test
    public void getUserTest() throws Exception {
        // given
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ModelFactory.createUser(entityManager, 209, null, null);
        transaction.commit();

        // give when
        User user = userManager.getUser(209);

        // then
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isEqualTo(209);
    }


    @Test
    public void createIfNotExistTestExist() throws Exception {
        // given
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ModelFactory.createUser(entityManager, 201, null, null);
        transaction.commit();
        int size = getUsersCount();

        // when
        userManager.createUserIfNotExist(201);

        // then
        int newSize = getUsersCount();
        Assert.assertEquals(newSize, size);
    }

    private int getUsersCount() {
        TypedQuery<User> query = entityManager.createQuery("select u from User u", User.class);
        return query.getResultList().size();
    }

    @Test
    public void createIfNotExistTestNotExist() throws Exception {
        // given when
        userManager.createUserIfNotExist(201);

        // then
        User user = userManager.getUser(201);
        Assertions.assertThat(user).isNotNull();
    }

}
