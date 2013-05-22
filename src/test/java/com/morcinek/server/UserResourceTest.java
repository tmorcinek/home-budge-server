package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.TestUser;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/api");

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @Test
    public void userNotFoundTest() {
        expect().
                statusCode(404).
                when().
                get("/user?password=tomek&email=karamba@1mmorcinek.com");
    }

    @Test
    public void userSuccessfullyCreated() {
        expect().
                statusCode(201).
                body("email", equalTo("karamba@morcinek.com"), "name", equalTo("morcin")).
                when().put("/user/create?password=tomek&email=karamba@morcinek.com&name=morcin");
    }

    @Test
    public void userUnsuccessfullyCreated() {
        expect().
                statusCode(400).
                when().put("/user/create?password=tomek&email=karamba@morcinek.com");
    }

    @Test
    public void userUnsuccessfullyCreated2() {
        expect().
                statusCode(400).
                when().put("/user/create?password=tomek&name=morcin");
    }

    @Test
    public void userUnsuccessfullyCreated3() {
        expect().
                statusCode(400).
                when().put("/user/create?email=karamba@morcinek.com&name=morcin");
    }

    @Test
    public void userConfirm() {
        expect().
                statusCode(501).
                when().put("/user/confirm?email=karamba@morcinek.com&name=morcin");
    }

    @Test
    public void userUpdate() {
        TestUser user = new TestUser("karamba@pl.com", "pass");
        given().
                contentType(ContentType.JSON).
                body(user).
                expect().
                statusCode(501).
                when().
                post("/user");
    }

    @Test
    public void userCreateJson() {
        TestUser user = new TestUser("karamba@pl.com", "pass");
        user.setName("morcin");
        given().
                contentType(ContentType.JSON).
                body(user).
                expect().
                body("email", equalTo("karamba@pl.com"), "name", equalTo("morcin"), "id", notNullValue()).
                statusCode(201).
                when().
                put("/user");
    }

}
