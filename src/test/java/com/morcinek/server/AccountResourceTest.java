package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.*;
import com.morcinek.server.webservice.util.SessionManager;
import org.fest.assertions.Assertions;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class AccountResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/test/api");
    private static Long accountId;
    private static Long accountId2;

    private static EntityManager entityManager;
    private static SessionManager sessionManager;

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/test/api";
    }

    @BeforeClass
    public static void initAccount() {
        entityManager = serverRule.getInjector().getInstance(EntityManager.class);
        sessionManager = serverRule.getInjector().getInstance(SessionManager.class);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        User user = ModelFactory.createUser(entityManager, 1, "tomek", "tomk@morcinek.com");
        accountId = ModelFactory.createAccount(entityManager, "Limanowskiego211", user).getId();
        ModelFactory.createUser(entityManager, 2, "marcin", "marcin@ogorek.com");
        ModelFactory.createUser(entityManager, 3, "klara", "takitam@costam.com");

        User maciek = ModelFactory.createUser(entityManager, 4, "maciek", "takitam@costam.com");
        User tola = ModelFactory.createUser(entityManager, 5, "tola", "banan@banan.com");
        User skapiec = ModelFactory.createUser(entityManager, 6, "skapiec", "skapiec@nowy.com");
        Account account = ModelFactory.createAccount(entityManager, "Name", maciek, tola, skapiec);
        accountId2 = account.getId();
        ModelFactory.createRecord(entityManager, account, 23.0, "Obiad", maciek, maciek, maciek, tola);
        ModelFactory.createRecord(entityManager, account, 25.0, "Pranie", tola, tola, maciek);

        tx.commit();
    }

    @Test
    public void getAccountTest() {
        sessionManager.validateToken("1");

        given().
                expect().
                statusCode(200).
                body("name", Matchers.is("Limanowskiego211")).
                body("users[0].name", Matchers.is("tomek")).
                body("users[0].email", Matchers.is("tomk@morcinek.com")).
                when().
                get("/accounts/" + accountId).asString();
    }

    @Test
    public void getAccountTestAuthorizationError() {
        sessionManager.validateToken("2");

        given().
                expect().
                statusCode(401).
                body("errorTitle", Matchers.equalTo("Authorization Error")).
                body("errorMessage", Matchers.equalTo("User do not own this account.")).
                when().
                get("/accounts/" + accountId);
    }

    @Test
    public void createAccountTest() {
        sessionManager.validateToken("1");

        TestAccount account = new TestAccount();
        TestUser user = new TestUser(3L);
        TestUser user2 = new TestUser(1L);
        account.addUser(user);
        account.addUser(user2);
        account.setName("Kawalerii 8");
        given().
                contentType(ContentType.JSON).
                body(account).
                expect().
                statusCode(201).
                when().
                post("/accounts");

        // then
        User user3 = entityManager.find(User.class, 3L);
        entityManager.refresh(user3);
        Assertions.assertThat(user3.getAccounts()).isNotNull().hasSize(1);
        Assertions.assertThat(user3.getAccounts().get(0).getName()).isEqualTo("Kawalerii 8");

    }

    @Test
    public void createAccountTestValidationError() {
        sessionManager.validateToken("1");

        TestAccount account = new TestAccount();
        TestUser user = new TestUser(3L);
        TestUser user2 = new TestUser(1L);
        account.addUser(user);
        account.addUser(user2);
        given().
                contentType(ContentType.JSON).
                body(account).
                expect().
                statusCode(403).
                body("errorTitle", Matchers.equalTo("Validation Error")).
                body("errorMessage", Matchers.equalTo("Account name cannot be empty.")).
                when().
                post("/accounts");

        // then
        User user3 = entityManager.find(User.class, 3L);
        entityManager.refresh(user3);
        Assertions.assertThat(user3.getAccounts()).isNotNull().hasSize(1);
        Assertions.assertThat(user3.getAccounts().get(0).getName()).isEqualTo("Kawalerii 8");

    }

    @Test
    public void addUsersToAccountTest() {
        List<TestUser> users = new ArrayList<>();
        users.add(new TestUser(1L));
        given().
                contentType(ContentType.JSON).
                body(users).
                expect().
                statusCode(200).
                when().
                put("/accounts/" + accountId + "/users");
    }

    @Test
    public void removeUserToAccountTestUnauthorized() {
        sessionManager.validateToken("4");

        given().
                expect().
                statusCode(401).
                body("errorTitle", Matchers.equalTo("Authorization Error")).
                body("errorMessage", Matchers.equalTo("User cannot create record for this account.")).
                when().
                delete("/accounts/" + accountId + "/users/3");
    }

    @Test
    public void removeUserToAccountTestValidationError() {
        sessionManager.validateToken("4");

        given().
                expect().
                statusCode(403).
                body("errorTitle", Matchers.equalTo("Validation Error")).
                body("errorMessage", Matchers.equalTo("User cannot be removed, due to financial involvement.")).
                when().
                delete("/accounts/" + accountId2 + "/users/4");
    }

    @Test
    public void removeUserToAccountTest() {
        sessionManager.validateToken("4");

        given().
                expect().
                statusCode(200).
                when().
                delete("/accounts/" + accountId2 + "/users/6");
    }

}
