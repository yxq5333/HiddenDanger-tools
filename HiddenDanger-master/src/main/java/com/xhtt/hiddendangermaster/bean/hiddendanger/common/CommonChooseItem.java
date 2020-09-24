package com.xhtt.hiddendangermaster.bean.hiddendanger.common;

import com.google.gson.annotations.SerializedName;

/**
 * 通用选项
 * <p>
 * Created by Hollow Goods on 2020-04-14.
 */
public class CommonChooseItem {

    private long id;

    @SerializedName(value = "name", alternate = {"value"})
    private String item;

    public CommonChooseItem setId(long id) {
        this.id = id;
        return this;
    }

    public CommonChooseItem setItem(String item) {
        this.item = item;
        return this;
    }

    public long getId() {
        return id;
    }

    public String getItem() {
        return item;
    }
}
