package com.morcinek.server.webservice.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/6/13
 * Time: 12:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataValidationException extends Exception {

    public DataValidationException() {
    }

    public DataValidationException(String message) {
        super(message);
    }
}
