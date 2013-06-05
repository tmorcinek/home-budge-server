package com.morcinek.server.database;

import com.google.inject.Singleton;
import com.morcinek.server.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/3/13
 * Time: 2:51 AM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class UserManager {

    @Inject
    private EntityManager entityManager;

    public User getUser(long userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            entityManager.refresh(user);
        }
        return user;
    }

    public User getUserByEmail(String email) {
        TypedQuery<User> namedQuery = entityManager.createNamedQuery("select u User u where u.email = :email", User.class);
        namedQuery.setParameter("email", email);
        return namedQuery.getSingleResult();
    }

    public List<User> getUsersByName(String name) {
        TypedQuery<User> namedQuery = entityManager.createNamedQuery("select u User u where u.name = :name", User.class);
        namedQuery.setParameter("name", name);
        return namedQuery.getResultList();
    }

    public void createUserIfNotExist(long userId) throws Exception {
        createUserIfNotExist(userId, null, null);
    }

    public void createUserIfNotExist(long userId, String name, String email) throws Exception {
        if (getUser(userId) != null) {
            return;
        }
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(new User(userId, email, name));
        entityManager.flush();
        tx.commit();
    }

    /**
     * @param
     * @return
     * @throws Exception
     * @see #updateUser(long, String, String)
     */
    public User updateUser(User user) throws Exception {
        return updateUser(user.getId(), user.getEmail(), user.getName());
    }

    /**
     * @param userId
     * @param email
     * @param name   can be null but not empty string
     * @return
     * @throws Exception
     */
    public User updateUser(long userId, String email, String name) throws Exception {
        User oldUser = getUser(userId);
        entityManager.refresh(oldUser);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        if (name != null) {
            oldUser.setName(name);
        }
        if (email != null) {
            oldUser.setEmail(email);
        }
        entityManager.merge(oldUser);
        entityManager.flush();
        tx.commit();
        return oldUser;
    }
}
