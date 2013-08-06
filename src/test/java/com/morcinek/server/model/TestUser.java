package com.morcinek.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 4/11/13
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TestUser {

    private Long id;

    private String email;

    private String name;

    private List<TestAccount> accounts;

    public TestUser() {
    }

    public TestUser(Long id) {
        this.id = id;
    }

    public TestUser(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
