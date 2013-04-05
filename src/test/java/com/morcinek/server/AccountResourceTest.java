package com.morcinek.server;

import com.google.inject.Inject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.guice.GuiceJUnitRunner;
import com.morcinek.server.webservice.resources.AccountResource;
import com.morcinek.server.webservice.resources.UserResource;
import org.junit.*;
import org.junit.runner.RunWith;

import static com.jayway.restassured.RestAssured.given;

@RunWith(GuiceJUnitRunner.class)
public class AccountResourceTest {

    @Rule
    public static ServerRule serverRule = new ServerRule(8080, "/api");

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @Inject
    private UserResource userResource;

    @Inject
    private AccountResource accountResource;

    @Before
    public void initAccount(){
        try {
            User user = new User();
            user.setName("tomek");
            user.setEmail("tomk@morcinek.com");
            user.setPassword("tomek");
            userResource.createUser(user);
            Account account = new Account();
            account.setName("Limanowskiego211");
            accountResource.createAccount(1, account);
        } catch (Exception e) {
        }
    }

    @Test
    public void getAccountUsersTest() {
        given().
                param("accountId", 2).
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
                param("userId", 1).
        expect().
                statusCode(201).
        when().
                put("/account");
    }

    @Test
    public void addUserToAccountTest() {
        given().
                queryParam("accountId", 2).
                queryParam("userId", 1).
                expect().
                statusCode(200).
                when().
                post("/account");
    }

    @Test
    public void removeUserToAccountTest() {
        given().
                param("accountId", 2).
                param("userId", 1).
                expect().
                statusCode(200).
                when().
                delete("/account");
    }

}
