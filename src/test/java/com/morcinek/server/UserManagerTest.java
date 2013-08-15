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
import java.util.List;

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
        ModelFactory.createUser(entityManager, null, null, null, 3234324L);
        transaction.commit();
        int size = getUsersCount();

        // when
        userManager.createUserIfNotExistFromFacebookId(3234324L);

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
        User user = userManager.createUserIfNotExistFromFacebookId(2020000L);

        // then
        Assertions.assertThat(user).isNotNull();
    }


    @Test
    public void createUserWithDetailsTest() throws Exception {
        // given when
        User user1 = userManager.createUserIfNotExistFromFacebookId(2030000);
        userManager.updateUser(user1.getId(), "morcinek@pl", "Tomasz");
        User user2 = userManager.createUserIfNotExistFromFacebookId(2040000);
        userManager.updateUser(user2.getId(), null, "Tomasz");
        User user3 = userManager.createUserIfNotExistFromFacebookId(2050000);
        userManager.updateUser(user3.getId(), "Morcinek@pl", null);
        User user4 = userManager.createUserIfNotExistFromFacebookId(2060000);
        userManager.updateUser(user4.getId(), "Morcinek@pl", "");

        // then
        validateUser(user1.getId(), "Tomasz", "morcinek@pl");
        validateUser(user2.getId(), "Tomasz", null);
        validateUser(user3.getId(), null, "Morcinek@pl");
        validateUser(user4.getId(), "", "Morcinek@pl");
    }

    private void validateUser(long userId, String name, String email) {
        User user = userManager.getUser(userId);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getName()).isEqualTo(name);
        Assertions.assertThat(user.getEmail()).isEqualTo(email);
    }


    @Test
    public void updateUserTest() throws Exception {
        // given
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ModelFactory.createUser(entityManager, 211, "Tomek", "tomasz.morcinek@pl");
        ModelFactory.createUser(entityManager, 212, "Tomek", "tomasz.morcinek@pl");
        ModelFactory.createUser(entityManager, 213, null, "tomasz.morcinek@pl");
        ModelFactory.createUser(entityManager, 214, "Tomek", "klar.mala@karolina.pl");
        transaction.commit();
        int size = getUsersCount();

        // when
        userManager.updateUser(211, null, "Marek");
        userManager.updateUser(211, "tomek@morcinek", null);
        userManager.updateUser(212, "jarek@malpa.pl", null);
        userManager.updateUser(213, "magda@mpk.com", "magda");
        userManager.updateUser(214, null, "Karolina");

        // then
        validateUser(211, "Marek", "tomek@morcinek");
        validateUser(212, "Tomek", "jarek@malpa.pl");
        validateUser(213, "magda", "magda@mpk.com");
        validateUser(214, "Karolina", "klar.mala@karolina.pl");
    }


    @Test
    public void updateUserTest2() throws Exception {
        // given
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ModelFactory.createUser(entityManager, 221, "Tomek", "tomasz.morcinek@pl");
        ModelFactory.createUser(entityManager, 222, "Tomek", "tomasz.morcinek@pl");
        ModelFactory.createUser(entityManager, 223, "Marek", "tomasz.morcinek@pl");
        ModelFactory.createUser(entityManager, 224, "Tomek", "mala@karolinakkk.pl");
        transaction.commit();
        int size = getUsersCount();

        // when
        userManager.updateUser(new User(221, null, "Marek"));
        userManager.updateUser(new User(221, null, "Barbara"));
        userManager.updateUser(new User(222, "jarek@malpa.pl", null));
        userManager.updateUser(new User(223, "magda@mpk.com", "magda"));
        userManager.updateUser(new User(224, null, "Karolina"));

        // then
        validateUser(221, "Barbara", "tomasz.morcinek@pl");
        validateUser(222, "Tomek", "jarek@malpa.pl");
        validateUser(223, "magda", "magda@mpk.com");
        validateUser(224, "Karolina", "mala@karolinakkk.pl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateUserTestNameEmptyText() throws Exception {

        // given when then
        userManager.updateUser(0, "djdklsf", "fjdklsajf");

    }

    @Test
    public void getUsersByNameTest() throws Exception {
        // given when
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ModelFactory.createUser(entityManager, 233, "Zonk", "morcinek@pl");
        ModelFactory.createUser(entityManager, 234, "Zonk Morcinek", "basia@poczta.pl");
        ModelFactory.createUser(entityManager, 235, "Maly Zonk", "kroper.malpa@pl");
        ModelFactory.createUser(entityManager, 236, "Krwawa zonk", "Morcinek@pl");
        ModelFactory.createUser(entityManager, 237, "Krwawa", "Morcinek@pl");
        transaction.commit();

        // then
        List<User> tomasz = userManager.getUsersByName("Zonk");
        Assertions.assertThat(tomasz).hasSize(4);
        for (User user : tomasz) {
            Assertions.assertThat(user.getName()).containsIgnoringCase("Zonk");
        }
    }

    @Test
    public void getUserByEmailTest() throws Exception {
        // given when
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ModelFactory.createUser(entityManager, 238, "Chyba ty", "twoja.starapl");
        ModelFactory.createUser(entityManager, 239, "maly Morcinek", "moja.twoja.stara@pl");
        ModelFactory.createUser(entityManager, 240, "Maly brzydal", "twoja.stara@pl.com");
        ModelFactory.createUser(entityManager, 241, "Krwawa ktos", "twoja.stara@pl");
        ModelFactory.createUser(entityManager, 242, "Krwawa", "maly@pl");
        transaction.commit();

        // then
        User user = userManager.getUserByEmail("twoja.stara@pl");
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getEmail()).isEqualTo("twoja.stara@pl");
        Assertions.assertThat(user.getName()).isEqualTo("Krwawa ktos");

    }

    @Test
    public void getUserIdFromSpecialIdTest() throws Exception {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        User user = ModelFactory.createUser(entityManager, null, "Chyba ty", "twoja.starapl", 12231L);
        User user2 = ModelFactory.createUser(entityManager, null, "mmnems", "nowy emails", 32342L);
        User user3 = ModelFactory.createUser(entityManager, null, "fajna bryka", "what is your name", 565777556L);
        transaction.commit();
        entityManager.refresh(user);

        // then
        Long userId = userManager.getUserIdFromFacebookId(12231);
        Assertions.assertThat(userId).isNotNull().isEqualTo(user.getId());
        Long userId2 = userManager.getUserIdFromFacebookId(32342L);
        Assertions.assertThat(userId2).isNotNull().isEqualTo(user2.getId());
        Long userId3 = userManager.getUserIdFromFacebookId(565777556L);
        Assertions.assertThat(userId3).isNotNull().isEqualTo(user3.getId());
    }


}
