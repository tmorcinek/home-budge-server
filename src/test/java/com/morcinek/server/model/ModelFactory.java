package com.morcinek.server.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/2/13
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelFactory {

    public static User createUser(EntityManager entityManager, long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        entityManager.persist(user);
        return user;
    }

    public static Account createAccount(EntityManager entityManager, String name, User... users) {
        Account account = new Account();
        account.setName(name);
        for (User user : users) {
            account.addUser(user);
        }
        entityManager.persist(account);
        return account;
    }


    public static Record createRecord(EntityManager entityManager, Account account, double ammount, String title, User creator, User payer, User... users) {
        Record record = new Record();
        record.setAccount(account);
        record.setAmount(ammount);
        record.setTitle(title);
        record.setCreator(creator);
        record.setPayer(payer);
        record.setUsers(Arrays.asList(users));
        if (entityManager != null) {
            entityManager.persist(record);
        }
        return record;
    }

    public static <T> T getObject(EntityManager entityManager, Class<T> type, long id) {
        T object = entityManager.find(type, id);
        entityManager.refresh(object);
        return object;
    }

    public static void removeObject(EntityManager entityManager, Object user1) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.refresh(user1);
        entityManager.remove(user1);
        transaction.commit();
    }


}
