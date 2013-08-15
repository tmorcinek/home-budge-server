package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.*;
import com.morcinek.server.webservice.util.SessionManager;
import com.morcinek.server.webservice.util.network.FakeWebGateway;
import org.fest.assertions.Assertions;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.jayway.restassured.RestAssured.given;

public class RecordResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/test/api");
    private static Long recordId;
    private static Long accountId;

    private static EntityManager entityManager;
    private static SessionManager sessionManager;

    private static Long recordId2;

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

        User user = ModelFactory.createUser(entityManager, 129, "tomek", "tomk1@morcinek.com");
        ModelFactory.createUser(entityManager, 130, "marek", "marek@major.com").getId();
        Account account = ModelFactory.createAccount(entityManager, "Limanowskiego211", user);
        accountId = account.getId();
        recordId = ModelFactory.createRecord(entityManager, account, 213.22, "zakupy", user, user, user).getId();
        recordId2 = ModelFactory.createRecord(entityManager, account, 490.12, "przekupy", "short description", user, user, user).getId();

        tx.commit();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // assert not changed fields
        User user = entityManager.find(User.class, 129L);
        Assertions.assertThat(user.getEmail()).isEqualTo("tomk1@morcinek.com");
        Assertions.assertThat(user.getName()).isEqualTo("tomek");
        User user2 = entityManager.find(User.class, 130L);
        Assertions.assertThat(user2.getEmail()).isEqualTo("marek@major.com");
        Assertions.assertThat(user2.getName()).isEqualTo("marek");

        Account account = entityManager.find(Account.class, accountId);
        Assertions.assertThat(account.getName()).isEqualTo("Limanowskiego211");
        Assertions.assertThat(account.getUsers()).hasSize(1);
        User actual = account.getUsers().toArray(new User[0])[0];
        Assertions.assertThat(actual).isEqualTo(user);

    }

    @Test
    public void getRecordListTest() {
        String accountId1 = given().
                param("accountId", accountId).
                expect().
                statusCode(200).
                when().
                get("records/accounts/" + accountId + "/records").asString();
        System.out.println(accountId1);
    }

    @Test
    public void createRecordTest() {
        User e = new User();
        sessionManager.validateToken("129");
        e.setId(129L);
        Record record = ModelFactory.createRecord(null, null, 513.78, "pierdoly do domu", e, e, e);
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(201).
                body("payer.id", Matchers.equalTo(129)).
                body("creator.id", Matchers.equalTo(129)).
                body("users[0].id", Matchers.equalTo(129)).
                body("title", Matchers.equalTo("pierdoly do domu")).
                body("amount", Matchers.equalTo(513.78f)).
                when().
                post("records/accounts/" + accountId + "/records");
    }

    @Test
    public void createRecordTestAuthorizationError() {
        User e = new User();
        sessionManager.validateToken("80001");
        e.setId(129L);
        Record record = ModelFactory.createRecord(null, null, 213.22, "zakupy", e, e, e);
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(401).
                body("errorTitle", Matchers.equalTo("Authorization Error")).
                body("errorMessage", Matchers.equalTo("User cannot create record for this account.")).
                when().
                post("records/accounts/" + accountId + "/records").asString();
    }

    @Test
    public void updateRecordTestAuthorizationError() {
        sessionManager.validateToken("80");

        TestRecord record = new TestRecord();
        record.setTitle("new title");
        record.setDescription("description");
        record.setUsers(null);
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(401).
                body("errorTitle", Matchers.equalTo("Authorization Error")).
                body("errorMessage", Matchers.equalTo("User cannot create record for this account.")).
                when().
                put("records/accounts/" + accountId + "/records/" + recordId);
    }

    @Test
    public void updateRecordTestErrorNoRecord() {
        sessionManager.validateToken("80");

        TestRecord record = new TestRecord();
        record.setTitle("new title");
        record.setDescription("description");
        record.setUsers(null);
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(403).
                body("errorTitle", Matchers.equalTo("Validation Error")).
                body("errorMessage", Matchers.equalTo("No record with such id.")).
                when().
                put("records/accounts/" + accountId + "/records/" + 12323);
    }

    @Test
    public void updateRecordTestErrorWrongAccount() {
        sessionManager.validateToken("129");

        TestRecord record = new TestRecord();
        record.setTitle("new title");
        record.setDescription("description");
        record.setUsers(null);
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(403).
                body("errorTitle", Matchers.equalTo("Validation Error")).
                body("errorMessage", Matchers.equalTo("Record does not exist in given account.")).
                when().
                put("records/accounts/" + 123 + "/records/" + recordId);
    }

    @Test
    public void updateRecordTest() {
        sessionManager.validateToken("129");

        TestRecord record = new TestRecord();
        record.setTitle("new title");
        record.setDescription("description");
        record.setAmount(1999.99);
        record.setUsers(null);
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(200).
                body("title", Matchers.equalTo("new title")).
                body("description", Matchers.equalTo("description")).
                body("amount", Matchers.equalTo(1999.99f)).
                when().
                put("records/accounts/" + accountId + "/records/" + recordId);
    }

    @Test
    public void updateRecordTestAmount() {
        sessionManager.validateToken("129");

        TestRecord record = new TestRecord();
        record.setTitle("new title");
        record.setDescription("longer description");
        record.setAmount(1999.99);
        record.getUsers().add(new TestUser(130L));
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(200).
                body("title", Matchers.equalTo("new title")).
                body("description", Matchers.equalTo("longer description")).
                body("amount", Matchers.equalTo(1999.99f)).
                body("users[0].id", Matchers.equalTo(130)).
                when().
                put("records/accounts/" + accountId + "/records/" + recordId2);
    }

    @Test
    public void updateRecordTestPayer() {
        sessionManager.validateToken("129");

        TestRecord record = new TestRecord();
        record.setPayer(new TestUser(130L));
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(record).
                expect().
                statusCode(200).
                body("payer.id", Matchers.equalTo(130)).
                when().
                put("records/accounts/" + accountId + "/records/" + recordId2);
    }

}
