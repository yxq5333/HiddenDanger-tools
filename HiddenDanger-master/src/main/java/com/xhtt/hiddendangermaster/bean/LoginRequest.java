package com.xhtt.hiddendangermaster.bean;

/**
 * Created by Hollow Goods on 2019-04-11.
 */
public class LoginRequest {

    private Object username;
    private Object password;

    public LoginRequest(Object username, Object password) {
        this.username = username;
        this.password = password;
    }
}
