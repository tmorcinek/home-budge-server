package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.*;
import org.fest.assertions.Assertions;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.jayway.restassured.RestAssured.given;

public class AccountResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/test/api");
    private static Long accountId;

    private static EntityManager entityManager;

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/test/api";
    }

    @BeforeClass
    public static void initAccount() {
        entityManager = serverRule.getInjector().getInstance(EntityManager.class);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        User user = ModelFactory.createUser(entityManager, 1, "tomek", "tomk@morcinek.com");
        Account account = ModelFactory.createAccount(entityManager, "Limanowskiego211", user);
        accountId = account.getId();
        ModelFactory.createUser(entityManager, 2, "marcin", "marcin@ogorek.com");
        ModelFactory.createUser(entityManager, 3, "klara", "takitam@costam.com");
        tx.commit();
    }

    @Test
    public void getAccountTest() {
        given().
        expect().
            statusCode(200).
            body("name", Matchers.is("Limanowskiego211")).
            body("users[0].name", Matchers.is("tomek")).
            body("users[0].email", Matchers.is("tomk@morcinek.com")).
        when().
            get("/account/" + accountId).asString();
    }

    @Test
    public void createAccountTest() {
        TestAccount account = new TestAccount();
        TestUser user = new TestUser(3L, null, null);
        TestUser user2 = new TestUser(1L, null, null);
        account.addUser(user);
        account.addUser(user2);
        account.setName("Kawalerii 8");
        given().
                contentType(ContentType.JSON).
                body(account).
                param("userId", 1).
        expect().
                statusCode(201).
                when().
                put("/account");

        // then
        User user3 = entityManager.find(User.class, 3L);
        entityManager.refresh(user3);
        Assertions.assertThat(user3.getAccounts()).isNotNull().hasSize(1);
        Assertions.assertThat(user3.getAccounts().get(0).getName()).isEqualTo("Kawalerii 8");

    }

    @Test
    public void addUserToAccountTest() {
        given().
                queryParam("accountId", accountId).
                queryParam("userId", 2).
        expect().
                statusCode(200).
                when().
                post("/account");
    }

    @Test
    public void removeUserToAccountTest() {
        given().
                param("accountId", accountId).
                param("userId", 1).
                expect().
                statusCode(200).
                when().
                delete("/account");
    }

}
