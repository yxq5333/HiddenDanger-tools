package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class CheckTableContent {

    /**** 符合 ****/
    public static final int STATUS_YES = 1;
    /**** 不符合 ****/
    public static final int STATUS_NO = 0;

    private long id;
    private long checkTableId;

    @SerializedName("content")
    private String checkContent;// 检查内容

    private String according;// 依据

    private Integer status;// 状态

    @SerializedName("webCompanyServiceRiskListEntity")
    private HiddenDanger hiddenDanger;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCheckTableId() {
        return checkTableId;
    }

    public void setCheckTableId(long checkTableId) {
        this.checkTableId = checkTableId;
    }

    public String getCheckContent() {
        return checkContent;
    }

    public void setCheckContent(String checkContent) {
        this.checkContent = checkContent;
    }

    public String getAccording() {
        return according;
    }

    public void setAccording(String according) {
        this.according = according;
    }

    public HiddenDanger getHiddenDanger() {
        return hiddenDanger;
    }

    public void setHiddenDanger(HiddenDanger hiddenDanger) {
        this.hiddenDanger = hiddenDanger;
    }
}
