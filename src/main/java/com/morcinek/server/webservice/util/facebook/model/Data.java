package com.morcinek.server.webservice.util.facebook.model;

import java.util.Date;

public class Data {

    private long app_id;

    private Error error;

    private boolean is_valid;

    private String application;

    private long user_id;

    private Date expires_at;

    private Date issued_at;

    public long getApp_id() {
        return app_id;
    }

    public Error getError() {
        return error;
    }

    public boolean isIs_valid() {
        return is_valid;
    }

    public String getApplication() {
        return application;
    }

    public long getUser_id() {
        return user_id;
    }

    public Date getExpires_at() {
        return expires_at;
    }

    public Date getIssued_at() {
        return issued_at;
    }
}
