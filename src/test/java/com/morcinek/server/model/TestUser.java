package com.morcinek.server.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 4/11/13
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class TestUser {

    private Long id;

    private String email;

    private String password;

    private String name;

    private List<TestAccount> accounts = new ArrayList<TestAccount>();

    public TestUser() {
    }

    public TestUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccounts(List<TestAccount> accounts) {
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public List<TestAccount> getAccounts() {
        return accounts;
    }


    public void addAccount(TestAccount testAccount) {
        accounts.add(testAccount);
    }
}
