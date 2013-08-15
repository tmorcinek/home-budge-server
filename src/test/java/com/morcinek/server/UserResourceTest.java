package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.ModelFactory;
import com.morcinek.server.model.TestUser;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.util.GenericParser;
import com.morcinek.server.webservice.util.SessionManager;
import com.morcinek.server.webservice.util.network.FakeWebGateway;
import org.fest.assertions.Assertions;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.core.Response;

import java.io.InputStream;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class UserResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/test/api");
    private static User tomek;
    private static SessionManager sessionManager;

    private static EntityManager entityManager;
    private static GenericParser genericParser;

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/test/api";
    }

    @BeforeClass
    public static void initUsers() throws Exception {
        injectFields();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        tomek = ModelFactory.createUser(entityManager, 301, "Tomek", "tomasz.morcinek@pl");
        ModelFactory.createUser(entityManager, 302, "Marika", "marika.mala@pl");
        ModelFactory.createUser(entityManager, 303, "loool", "nie.wiem.co.napisac@pl");
        ModelFactory.createUser(entityManager, 304, "Milton Friedman", "mala@karolina.pl");
        ModelFactory.createUser(entityManager, 305, "mloool tomek", "amala@karolina.pl");
        ModelFactory.createUser(entityManager, 306, "gloool mma", "mala@karolina.pl.com");
        ModelFactory.createUser(entityManager, 307, "loool barbara", "malaa@karolina.pl");
        transaction.commit();
    }

    private static void injectFields() {
        entityManager = serverRule.getInjector().getInstance(EntityManager.class);
        sessionManager = serverRule.getInjector().getInstance(SessionManager.class);
        genericParser = serverRule.getInjector().getInstance(GenericParser.class);
    }

    @Test
    public void userNotFoundPathTest() {
        given().
                expect().
                statusCode(Response.Status.BAD_REQUEST.getStatusCode()).
                when().
                get("/users/0");
    }

    @Test
    public void userFoundPath() {
        sessionManager.validateToken("301");

        given().
                expect().
                statusCode(200).
                body("id", Matchers.is(301)).
                body("name", Matchers.is("Tomek")).
                body("email", Matchers.is("tomasz.morcinek@pl")).
                when().
                get("/users/301");
    }

    @Test
    public void userFound() {
        given().
                expect().
                statusCode(200).
                body("id", Matchers.is(301)).
                body("name", Matchers.is("Tomek")).
                body("email", Matchers.is("tomasz.morcinek@pl")).
                when().
                get("/users/301");
    }

    @Test
    public void registerUser() {
        // given (Authentication)
        sessionManager.validateToken("9207059");

        // when (registration of new user)
        TestUser user = new TestUser(9207059L, "gienek.loska@gmail.com", "Eugeniusz");
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK_2).
                contentType(ContentType.JSON).
                body(user).
                expect().
                statusCode(200).
                when().
                post("/users/me");

        // then
        User user1 = ModelFactory.getObject(entityManager, User.class, 9207059L);
        Assert.assertEquals("gienek.loska@gmail.com", user1.getEmail());
        Assert.assertEquals("Eugeniusz", user1.getName());

    }

    @Test
    public void updateUser() {
        // Authentication
        sessionManager.validateToken("1207059");
        // registration of new user
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ModelFactory.createUser(entityManager, 1207059L, "tomasz.morcinek@gmail.com", "Tomasz Morcinek");
        transaction.commit();

        // when (updating user)
        TestUser user = new TestUser(1207059L, "karamba@pl.com", "modifiedUser");
        given().
                header("accessToken", FakeWebGateway.ACCESS_TOKEN_OK).
                contentType(ContentType.JSON).
                body(user).
        expect().
                statusCode(200).
        when().
                put("/users/me");

        // then
        User user1 = ModelFactory.getObject(entityManager, User.class, 1207059L);
        Assert.assertEquals("karamba@pl.com", user1.getEmail());
        Assert.assertEquals("modifiedUser", user1.getName());
    }

    @Test
    public void getUserByEmailTest() throws Exception {
        given().
                param("email","mala@karolina.pl").
        expect().
                body("email",Matchers.is("mala@karolina.pl")).
                body("name",Matchers.is("Milton Friedman")).
                body("id",Matchers.is(304)).
                statusCode(200).
        when().
                get("/users/search");

    }

    @Test
    public void getUserByName() throws Exception {
        String jsonString = given().
                param("name", "loool").
                expect().
                statusCode(200).
                when().
                get("/users/search").asString();

        List<User> users = genericParser.parseList(jsonString, User.class);
        Assertions.assertThat(users).hasSize(4);
    }
}
