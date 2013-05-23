package com.morcinek.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 5/23/13
 * Time: 1:47 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Author {

    private String firstName;

    private String lastName;

    private String email;

    private String www;

    public Author() {
    }

    public Author(String firstName, String lastName, String email, String www) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.www = www;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getWww() {
        return www;
    }
}
