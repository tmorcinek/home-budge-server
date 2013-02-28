package com.morcinek.server.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@XmlRootElement
@NamedNativeQueries(
        @NamedNativeQuery(name = "findAccountById", query = "SELECT * FROM Account a WHERE a.id = :id", resultClass = Account.class)
)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    @ManyToOne
    private User admin;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<User>();

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
        admin.addAccount(this);
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

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
        user.addAccount(this);
    }

    public User getAdmin() {
        return admin;
    }
}
