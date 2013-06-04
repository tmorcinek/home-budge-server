package com.morcinek.server;

import com.jayway.restassured.RestAssured;
import com.morcinek.server.model.Account;
import com.morcinek.server.model.Balance;
import com.morcinek.server.model.ModelFactory;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.util.GenericParser;
import org.fest.assertions.Assertions;
import org.fest.assertions.Delta;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class BalanceResourceTest {

    @ClassRule
    public static ServerRule serverRule = new ServerRule(8080, "/api");

    private static GenericParser genericParser = new GenericParser();

    private static Long accountId;

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @BeforeClass
    public static void initAccount() {
        EntityManager entityManager = serverRule.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        User user = ModelFactory.createUser(entityManager, 101, "marek", "marciasan@morcinek.com");
        User user1 = ModelFactory.createUser(entityManager, 102, "maniek", "maniek@morcinek.com");
        User user2 = ModelFactory.createUser(entityManager, 103, "tomislaw", "tomislaw@morcinek.com");
        Account account = ModelFactory.createAccount(entityManager, "Limanowskiego 33", user, user1, user2);
        accountId = account.getId();
        ModelFactory.createRecord(entityManager, account, 213.22, "zakupy", user, user, user, user1, user2);
        ModelFactory.createRecord(entityManager, account, 13, "Papier toaletowy", user, user1, user, user1, user2);
        ModelFactory.createRecord(entityManager, account, 10.56, "Piwa w biedronce", user, user2, user, user1, user2);
        tx.commit();
    }

    @Test
    public void getTotalBalanceTest() {
        given().
                param("accountId", accountId).
                expect().
                body("balance", Matchers.is("236.78")).
                statusCode(200).
                when().
                get("/balance/total");
    }

    @Test
    public void getBalancesTest() {
        String jsonString = given().
                param("accountId", accountId).
                expect().
                statusCode(200).
                when().
                get("/balance").asString();

        List<Balance> balances = genericParser.parseList(jsonString, Balance.class);

        //then
        Assertions.assertThat(balances).hasSize(3);
        for (Balance balance : balances) {
            if (balance.getUserId() == 101) {
                Assertions.assertThat(balance.getBalance()).isEqualTo(134.29, Delta.delta(0.01));
            } else if (balance.getUserId() == 102) {
                Assertions.assertThat(balance.getBalance()).isEqualTo(-65.92, Delta.delta(0.01));
            } else if (balance.getUserId() == 103) {
                Assertions.assertThat(balance.getBalance()).isEqualTo(-68.36, Delta.delta(0.01));
            }
        }

    }
}
