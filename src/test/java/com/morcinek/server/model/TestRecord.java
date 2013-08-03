package com.morcinek.server.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 4/5/13
 * Time: 7:15 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class TestRecord {

    private Long id;

    private TestUser creator;

    private TestUser payer;

    private String title;

    private String description;

    private Double amount;

    @JsonIgnore
    private TestAccount account;

    private List<TestUser> users = new ArrayList<TestUser>();

    public TestUser getCreator() {
        return creator;
    }

    public void setCreator(TestUser creator) {
        this.creator = creator;
    }

    public TestUser getPayer() {
        return payer;
    }

    public void setPayer(TestUser payer) {
        this.payer = payer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TestAccount getAccount() {
        return account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<TestUser> getUsers() {
        return users;
    }

    public void setUsers(List<TestUser> users) {
        this.users = users;
    }

    public void setAccount(TestAccount account) {
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
