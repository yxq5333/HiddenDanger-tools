package com.xhtt.hiddendangermaster.bean;

/**
 * 注册请求类
 * Created by Hollow Goods on 2019-04-11.
 */
public class RegisterRequest {

    private Object mobile;
    private Object name;
    private Object username;
    private Object password;

    public RegisterRequest(Object mobile, Object name, Object username, Object password) {
        this.mobile = mobile;
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
