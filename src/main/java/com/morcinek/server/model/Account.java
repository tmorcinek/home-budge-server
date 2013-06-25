package com.morcinek.server.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = true)
    private String description;

    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<User> users = new HashSet<>();

    @XmlTransient
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Record> records = new ArrayList<Record>();

    @PrePersist
    public void updateStartDate() {
        this.startDate = Calendar.getInstance();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
        user.addAccount(this);
    }

    public List<Record> getRecords() {
        return records;
    }


    public void addRecord(Record record) {
        records.add(record);
        record.setAccount(this);
    }

    public String getDescription() {
        return description;
    }
}
