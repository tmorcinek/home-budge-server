package com.morcinek.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/2/13
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Balance {

    private Long userId;

    private double balance;

    public Balance() {
    }

    public Balance(Long userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void addToBalance(double balance){
        this.balance += balance;
    }

}
