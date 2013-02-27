package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.Account;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;

public class AccountResourceTest {
	@Rule
	public static ServerRule serverRule = new ServerRule(8080,"/api");

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @Test
    public void getAccountUsersTest() {
        given().
                param("accountId",1).
        expect().
                statusCode(200).
        when().
                get("/account");
    }

    @Test
    public void createAccountTest() {
        Account account = new Account();
        account.setName("Limanowskiego");
        given().
                contentType(ContentType.JSON).
                body(account).
                param("userId",1).
        expect().
                statusCode(201).
        when().
                put("/account");
    }

    @Test
    public void addUserToAccountTest() {
        given().
                queryParam("accountId",1).
                queryParam("userId",12).
        expect().
                statusCode(200).
        when().
                post("/account");
    }

    @Test
    public void removeUserToAccountTest() {
        given().
                param("accountId",1).
                param("userId",1).
        expect().
                statusCode(200).
        when().
                delete("/account");
    }

}
