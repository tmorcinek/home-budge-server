package com.morcinek.server.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@NamedNativeQueries(
        @NamedNativeQuery(name = "findAccountById", query = "SELECT * FROM account WHERE account.id = ?", resultClass = User.class)
)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Date startDate;

    @ManyToOne
    private User admin;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<User>();

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
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

    public Date getStartDate() {
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
