package com.morcinek.server;

import com.google.inject.Inject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.Record;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.guice.GuiceJUnitRunner;
import com.morcinek.server.webservice.resources.AccountResource;
import com.morcinek.server.webservice.resources.RecordResource;
import com.morcinek.server.webservice.resources.UserResource;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class RecordResourceTest {

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
    }

    @Test
    public void getRecordByIdTest() {
        given().
                expect().
                statusCode(200).
                when().
                get("/record/3");

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
        Record record = new Record();
        record.setAmount(213.22);
        record.setTitle("zakupy");
        Account account = new Account();
        account.setId(2L);
        User e = new User();
        e.setId(1L);

        record.setCreator(e);
        record.setPayer(e);
        ArrayList<User> users = new ArrayList<User>();
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
        Record record = new Record();
        Account account = new Account();
        account.setId(2L);
        User e = new User();
        e.setId(1L);
        record.setAccount(account);
        record.setCreator(e);
        record.setPayer(e);
        ArrayList<User> users = new ArrayList<User>();
        users.add(e);
        record.setUsers(users);
        given().
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(200).
                when().
                post("/record");
    }
}
