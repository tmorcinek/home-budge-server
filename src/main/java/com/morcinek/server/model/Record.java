package com.morcinek.server.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Calendar;
import java.util.Date;
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
@XmlAccessorType(value = XmlAccessType.FIELD)
@NamedQuery(name = "findRecordById", query = "SELECT r FROM Record r WHERE r.id = :id")
public class Record {

    @Id
    @GeneratedValue
    private Long id;

    // TODO please fix it later not to be opitonal
    // this is optional only because there is no token system yet.
    @ManyToOne(optional = true)
    private User creator;

    @ManyToOne(optional = false)
    private User payer;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedDate;

    @Basic(optional = false)
    private String title;

    private String description;

    @Basic(optional = false)
    private Double amount;

    @XmlTransient
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false, fetch = FetchType.LAZY)
    private Account account;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<User> users;

    @PreUpdate
    public void updateDate() {
        this.updatedDate = Calendar.getInstance();
    }

    @PrePersist
    public void createDate() {
        this.createdDate = Calendar.getInstance();
        this.updatedDate = this.createdDate;
    }

    public User getCreator() {
        return creator;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public Calendar getUpdatedDate() {
        return updatedDate;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public Long getId() {
        return id;
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
