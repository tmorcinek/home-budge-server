package com.morcinek.server.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 4/5/13
 * Time: 7:15 PM
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Basic(optional = false)
    private String title;

    private String description;

    @Basic(optional = false)
    private Double amount;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    private Account account;

    @ManyToMany()
    private List<User> users;

    @PreUpdate
    public void updateDate() {
        this.updatedDate = new Date();
    }

    @PrePersist
    public void createDate() {
        this.createdDate = new Date();
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getUpdatedDate() {
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
