package com.morcinek.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/2/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class WebserviceError {

    private Integer errorCode;

    private String errorMessage;

    public WebserviceError() {
    }

    public WebserviceError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
