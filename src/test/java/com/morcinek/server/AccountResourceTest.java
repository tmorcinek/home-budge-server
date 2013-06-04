package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.ModelFactory;
import com.morcinek.server.model.TestAccount;
import com.morcinek.server.model.User;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.jayway.restassured.RestAssured.given;

public class AccountResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/api");
    private static Long accountId;

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @BeforeClass
    public static void initAccount() {
        EntityManager entityManager = serverRule.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        User user = ModelFactory.createUser(entityManager, 1, "tomek", "tomk@morcinek.com");
        Account account = ModelFactory.createAccount(entityManager, "Limanowskiego211", user);
        accountId = account.getId();
        ModelFactory.createUser(entityManager, 2, "marcin", "marcin@ogorek.com");
        tx.commit();
    }

    @Test
    public void getAccountUsersTest() {
        given().
                param("accountId", accountId).
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
