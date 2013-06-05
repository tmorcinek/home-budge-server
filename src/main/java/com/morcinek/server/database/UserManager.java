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
        TypedQuery<User> namedQuery = entityManager.createQuery("select u from User u where u.email = :email", User.class);
        namedQuery.setParameter("email", email);
        User singleResult = namedQuery.getSingleResult();
        if (singleResult != null) {
            prepareUser(singleResult);
        }
        return singleResult;
    }

    private void prepareUser(User singleResult) {
        entityManager.refresh(singleResult);
        singleResult.setAccounts(null);
    }

    public List<User> getUsersByName(String name) {
        TypedQuery<User> namedQuery = entityManager.createQuery("select u from User u where u.name like :name", User.class);
        namedQuery.setParameter("name", "%" + name + "%");
        List<User> resultList = namedQuery.getResultList();
        for (User user : resultList) {
            prepareUser(user);
        }
        return resultList;
    }

    public boolean createUserIfNotExist(long userId) throws Exception {
        return createUserIfNotExist(userId, null, null);
    }

    public boolean createUserIfNotExist(long userId, String name, String email) throws Exception {
        if (getUser(userId) != null) {
            return false;
        }
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(new User(userId, email, name));
        tx.commit();
        return true;
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
        tx.commit();
        entityManager.detach(oldUser);
        oldUser.setAccounts(null);
        return oldUser;
    }
}
