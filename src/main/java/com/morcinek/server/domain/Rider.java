package com.morcinek.server.domain;


import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 2/10/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Rider {

    //For SQLite use GenerationType.AUTO to generate id
    //for derby, H2, MySQL etc use GenerationType.IDENTITY

    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private Long id;
    private String name;
    private String phone;
    private double distance;

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
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }


    //this is optional, just for print out into console
    @Override
    public String toString() {
        return "Rider [id=" + id + ", name=" + name + ", phone=" + phone + ", distance=" + distance + "]";
    }

}
