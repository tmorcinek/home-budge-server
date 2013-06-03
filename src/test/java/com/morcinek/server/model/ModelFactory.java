package com.morcinek.server.model;

import javax.persistence.EntityManager;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/2/13
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelFactory {

    public static User createUser(EntityManager entityManager, String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    public static Account createAccount(EntityManager entityManager, String name,  User... users) {
        Account account = new Account();
        account.setName(name);
        for (User user : users) {
            account.addUser(user);
        }
        entityManager.persist(account);
        entityManager.flush();
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
        entityManager.persist(record);
        entityManager.flush();
        return record;
    }
}