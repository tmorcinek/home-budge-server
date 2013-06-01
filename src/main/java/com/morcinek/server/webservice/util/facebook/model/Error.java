package com.morcinek.server.webservice.util.facebook.model;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/1/13
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Error {

    private String message;

    private int code;

    private int subcode;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public int getSubcode() {
        return subcode;
    }

}
