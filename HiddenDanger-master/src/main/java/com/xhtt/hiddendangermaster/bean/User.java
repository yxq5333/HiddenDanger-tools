package com.xhtt.hiddendangermaster.bean;

import com.google.gson.annotations.SerializedName;
import com.hg.hollowgoods.bean.CommonBean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;

/**
 * Created by Hollow Goods on 2019-04-04.
 */
@Table(name = "User")
public class User extends CommonBean<User> {

    public static final int USER_TYPE_GOV = 4;

    @Column(name = "tableId", isId = true)
    private int tableId;

    @Column(name = "username")
    private String username;

    private String name;

    @SerializedName("mobile")
    private String phone;

    private String alterPassword;

    private String clearCache;

    private String help;

    private String aboutUs;

    @Column(name = "password")
    private String password;

    private ArrayList<Integer> roleIdList;

    public ArrayList<Integer> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(ArrayList<Integer> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public User() {
        super(-1);
    }

    public User(int itemType) {
        super(itemType);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAlterPassword() {
        return alterPassword;
    }

    public void setAlterPassword(String alterPassword) {
        this.alterPassword = alterPassword;
    }

    public String getClearCache() {
        return clearCache;
    }

    public void setClearCache(String clearCache) {
        this.clearCache = clearCache;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
