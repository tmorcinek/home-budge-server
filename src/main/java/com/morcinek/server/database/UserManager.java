package com.morcinek.server.database;

import com.google.inject.Singleton;
import com.morcinek.server.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/3/13
 * Time: 2:51 AM
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

    public User createUserIfNotExistFromFacebookId(long facebookId) {
        Logger.getLogger("facebook").info("Facebook id: " + facebookId);
        User user = getUserFromFacebookId(facebookId);
        if (user == null) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            user = new User();
            user.setFacebookId(facebookId);
            entityManager.persist(user);
            Logger.getLogger("facebook").info("Facebook user: " + user);
            transaction.commit();
        }
        entityManager.refresh(user);
        return user;
    }

    public User updateUser(User user) throws Exception {
        return updateUser(user.getId(), user.getEmail(), user.getName());
    }

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
        entityManager.refresh(oldUser);
        entityManager.detach(oldUser);
        oldUser.setAccounts(null);

        return oldUser;
    }

    public User getUserFromFacebookId(long specialId) {
        TypedQuery<User> namedQuery = entityManager.createQuery("select u from User u where u.facebookId = :specialId", User.class);
        namedQuery.setParameter("specialId", specialId);
        try {
            return namedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getUserIdFromFacebookId(long specialId) {
        TypedQuery<Long> namedQuery = entityManager.createQuery("select u.id from User u where u.facebookId = :specialId", Long.class);
        namedQuery.setParameter("specialId", specialId);
        return namedQuery.getSingleResult();
    }
}
