package com.morcinek.server.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 2/10/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@XmlRootElement
@NamedNativeQuery(name = "findUserByEmailAndPassword", query = "SELECT * FROM user WHERE email = ? AND password = ?", resultClass = User.class)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
    private List<Account> accounts = new ArrayList<Account>();

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}
