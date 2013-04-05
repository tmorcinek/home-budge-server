package com.morcinek.server.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
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
@Entity
@XmlRootElement
public class Record {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private User creator;

    @ManyToOne(optional = false)
    private User payer;

    @Temporal(TemporalType.DATE)
    private Calendar createdDate;

    @Basic(optional = false)
    private String title;

    private String description;

    @Basic(optional = false)
    private Double amount;

    @ManyToOne(optional = false)
    private Account account;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<User> users = new ArrayList<User>();

    @PreUpdate
    @PrePersist
    public void updateStartDate() {
        this.createdDate = Calendar.getInstance();
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
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

    public Account getAccount() {
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
