package com.morcinek.server.database;

import com.google.inject.Singleton;
import com.morcinek.server.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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

    public void createUserIfNotExist(long userId) throws Exception {
        createUserIfNotExist(userId, null, null);
    }

    public void createUserIfNotExist(long userId, String email, String name) throws Exception {
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
     * @param user (User) value <code>name</code> can be null but not empty string and <code>email</code> can be either
     * @return
     * @throws Exception
     */
    public User updateUser(User user) throws Exception {
        User oldUser = getUser(user.getId());
        entityManager.refresh(oldUser);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        oldUser.setEmail(user.getEmail());
        entityManager.merge(oldUser);
        entityManager.flush();
        tx.commit();
        return oldUser;
    }
}
