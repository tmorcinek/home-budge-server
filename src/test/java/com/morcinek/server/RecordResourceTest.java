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
        user.setEmail("tomk1@morcinek.com");
        user.setPassword("tomek");
        entityManager.persist(user);
        Account account = new Account();
        account.setName("Limanowskiego211");
        account.setAdmin(user);
        entityManager.persist(account);
        Record record = new Record();
        record.setAccount(account);
        record.setAmount(213.22);
        record.setTitle("zakupy");
        record.setCreator(user);
        record.setPayer(user);
        ArrayList<User> users = new ArrayList<User>();
        users.add(user);
        record.setUsers(users);
        entityManager.persist(record);
        entityManager.flush();
        tx.commit();
        recordId = record.getId();
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
                param("accountId", 2).
                expect().
                statusCode(200).
                when().
                get("/record");
    }

    @Test
    public void createRecordTest() {
        TestRecord record = new TestRecord();
        record.setAmount(213.22);
        record.setTitle("zakupy");
        TestAccount account = new TestAccount();
        account.setId(2L);
        TestUser e = new TestUser();
        e.setId(1L);

        record.setCreator(e);
        record.setPayer(e);
        ArrayList<TestUser> users = new ArrayList<TestUser>();
        users.add(e);
        record.setUsers(users);
        record.setAccount(account);
        given().
                param("accountId", 2).
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
