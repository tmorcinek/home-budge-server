package com.morcinek.server;

import com.google.inject.Inject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.TestAccount;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.guice.GuiceJUnitRunner;
import com.morcinek.server.webservice.resources.AccountResource;
import com.morcinek.server.webservice.resources.UserResource;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static com.jayway.restassured.RestAssured.given;

public class AccountResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/api");

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }


    @BeforeClass
    public static void initAccount() {
        EntityManager entityManager = serverRule.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        User user = new User();
        user.setName("tomek");
        user.setEmail("tomk@morcinek.com");
        user.setPassword("tomek");
        entityManager.persist(user);
        entityManager.flush();
        Account account = new Account();
        account.setName("Limanowskiego211");
        account.setAdmin(user);
        entityManager.persist(account);
        entityManager.flush();
        tx.commit();
    }

    @Test
    public void getAccountUsersTest() {
        given().
                param("accountId", 2).
                expect().
                statusCode(200).
                when().
                get("/account");
    }

    @Test
    public void createAccountTest() {
        TestAccount account = new TestAccount();
        account.setName("Kawalerii 8");
        given().
                contentType(ContentType.JSON).
                body(account).
                param("userId", 1).
                expect().
                statusCode(201).
                when().
                put("/account");
    }

    @Test
    public void addUserToAccountTest() {
        given().
                queryParam("accountId", 2).
                queryParam("userId", 1).
                expect().
                statusCode(200).
                when().
                post("/account");
    }

    @Test
    public void removeUserToAccountTest() {
        given().
                param("accountId", 2).
                param("userId", 1).
                expect().
                statusCode(200).
                when().
                delete("/account");
    }

}
