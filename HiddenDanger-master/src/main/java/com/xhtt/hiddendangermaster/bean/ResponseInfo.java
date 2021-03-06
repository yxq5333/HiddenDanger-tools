package com.xhtt.hiddendangermaster.bean;

import com.hg.zero.net.callback.contract.ZResponseInfo;
import com.xhtt.hiddendangermaster.bean.knowledgebase.ResponseList;

/**
 * Created by Hollow Goods on 2019-04-11.
 */
public class ResponseInfo implements ZResponseInfo {

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
    private ResponseList page;

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

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public <T> T getTag() {
        return null;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseList getPage() {
        return page;
    }

    public void setPage(ResponseList page) {
        this.page = page;
    }
}
