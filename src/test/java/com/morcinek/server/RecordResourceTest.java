package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.morcinek.server.model.*;
import com.morcinek.server.webservice.util.SessionManager;
import com.morcinek.server.webservice.util.network.FakeWebGateway;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.given;

public class RecordResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/test/api");
    private static Long recordId;
    private static Long accountId;

    private static EntityManager entityManager;
    private static SessionManager sessionManager;

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/test/api";
    }

    @BeforeClass
    public static void injectFields() {
        sessionManager = serverRule.getInjector().getInstance(SessionManager.class);
        entityManager = serverRule.getInjector().getInstance(EntityManager.class);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        User user = ModelFactory.createUser(entityManager, 29, "tomek", "tomk1@morcinek.com");
        Account account = ModelFactory.createAccount(entityManager,"Limanowskiego211", user);
        accountId = account.getId();
        recordId = ModelFactory.createRecord(entityManager,account,213.22,"zakupy",user,user, user).getId();
        tx.commit();
    }

    @Test
    public void getRecordByIdTest() {
        given().
                expect().
                statusCode(200).
                when().
                get("/record/" + recordId);

    }

    @Test
    public void getRecordListTest() {
        String accountId1 = given().
                param("accountId", accountId).
                expect().
                statusCode(200).
                when().
                get("/record").asString();
        System.out.println(accountId1);
    }

    @Test
    public void createRecordTest() {
        User e = new User();
        sessionManager.validateToken("80001L");
        e.setId(80001L);
        e.setId(29L);
        Record record = ModelFactory.createRecord(null, null, 213.22, "zakupy", e, e, e);
        String json = given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                param("accountId", accountId).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(201).
                body("payer.id", Matchers.equalTo(29)).
                body("title", Matchers.is("zakupy")).
                body("amount", Matchers.equalTo(213.22f)).
                when().
                put("/record").asString();
        System.out.println(json);
    }

    @Test
    public void updateRecordTest() {
        TestRecord record = new TestRecord();
        record.setId(recordId);
        record.setTitle("new title");
        record.setDescription("description");
        record.setUsers(null);
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(200).
                when().
                post("/record");
    }
}
