package com.morcinek.server.model;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class TestAccount {

    private Long id;

    private String name;

    private Date startDate;

    private List<TestUser> users = new ArrayList<TestUser>();

    private List<TestRecord> records = new ArrayList<TestRecord>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addUser(TestUser user) {
        this.users.add(user);
    }

    public List<TestUser> getUsers() {
        return users;
    }
}
