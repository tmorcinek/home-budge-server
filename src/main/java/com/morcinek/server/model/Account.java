package com.morcinek.server.model;

import com.sun.istack.NotNull;
import org.eclipse.persistence.annotations.PrimaryKey;
import org.eclipse.persistence.internal.sessions.DirectCollectionChangeRecord;

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
        @NamedNativeQuery(name = "findAccountById", query = "SELECT * FROM ACCOUNT a WHERE a.id = ?id", resultClass = Account.class)
)
@Table(name = "ACCOUNT", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "admin"})})
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional = false)
    private String name;

    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    @ManyToOne(optional = false)
    private User admin;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<User> users = new ArrayList<User>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Record> records = new ArrayList<Record>();

    @PreUpdate
    public void updateStartDate(){
        this.startDate = Calendar.getInstance();
    }

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

    public void setRecords(List<Record> records) {
        this.records = records;
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

    public List<Record> getRecords() {
        return records;
    }


    public void addRecord(Record record) {
        records.add(record);
        record.setAccount(this);
    }
}
