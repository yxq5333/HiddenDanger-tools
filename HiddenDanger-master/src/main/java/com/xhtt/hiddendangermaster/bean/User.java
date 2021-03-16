package com.xhtt.hiddendangermaster.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.hg.zero.bean.ZCommonBean;

import java.util.ArrayList;

/**
 * Created by Hollow Goods on 2019-04-04.
 */
@Entity
public class User extends ZCommonBean<User> {

    public static final int USER_TYPE_GOV = 4;

    @ColumnInfo()
    @PrimaryKey(autoGenerate = true)
    private int tableId;

    @ColumnInfo
    private String username;

    private String name;

    @ColumnInfo
    @SerializedName("mobile")
    private String phone;

    private String alterPassword;

    private String clearCache;

    private String help;

    private String aboutUs;

    @ColumnInfo
    private String password;

    @Ignore
    private ArrayList<Integer> roleIdList;

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public ArrayList<Integer> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(ArrayList<Integer> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public User() {
        super(-1);
    }

    @Ignore
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
