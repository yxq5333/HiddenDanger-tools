package com.xhtt.hiddendangermaster.bean;

/**
 * Created by Hollow Goods on 2019-04-11.
 */
public class ResponseInfoSpecial {

    /**** 请求成功 ****/
    public static final int CODE_SUCCESS = 0;
    /**** 请求失败 ****/
    public static final int CODE_FAIL = 500;
    /**** Token过期 ****/
    public static final int CODE_TOKEN_OVERDUE = 401;

    private int code;
    private String msg;
    private String token;
    private Object data;
    private Object page;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }
}
