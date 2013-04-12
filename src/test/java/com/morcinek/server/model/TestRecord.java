package com.morcinek.server.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Calendar;
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

    private Calendar createdDate;

    private String title;

    private String description;

    private Double amount;

    @XmlTransient
    private TestAccount account;

    private List<TestUser> users = new ArrayList<TestUser>();

    public TestUser getCreator() {
        return creator;
    }

    public void setCreator(TestUser creator) {
        this.creator = creator;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public TestUser getPayer() {
        return payer;
    }

    public void setPayer(TestUser payer) {
        this.payer = payer;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public TestAccount getAccount() {
        return account;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
