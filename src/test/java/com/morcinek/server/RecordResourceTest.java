package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.*;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.given;

public class RecordResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/api");
    private static Long recordId;
    private static Long accountId;

    private static EntityManager entityManager;

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @BeforeClass
    public static void injectFields() {
        entityManager = serverRule.getInjector().getInstance(EntityManager.class);
    }

    @BeforeClass
    public static void initAccount() {
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
        given().
                param("accountId", accountId).
                expect().
                statusCode(200).
                when().
                get("/record");
    }

    @Test
    public void createRecordTest() {
        User e = new User();
        e.setId(29L);
        Record record = ModelFactory.createRecord(null, null, 213.22, "zakupy", e, e, e);
        given().
                param("accountId", accountId).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(201).
                when().
                put("/record");
    }

    @Test
    public void updateRecordTest() {
        TestRecord record = new TestRecord();
        record.setId(recordId);
        record.setTitle("new title");
        record.setDescription("description");
        record.setUsers(null);
        given().
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(200).
                when().
                post("/record");
    }
}
