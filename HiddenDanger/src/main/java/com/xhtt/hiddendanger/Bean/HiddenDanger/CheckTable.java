package com.xhtt.hiddendanger.Bean.HiddenDanger;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class CheckTable implements Serializable {

    /**** 未检查 ****/
    public static final int STATUS_UNCHECKED = 0;
    /**** 检查中 ****/
    public static final int STATUS_CHECK_ING = 1;
    /**** 已检查 ****/
    public static final int STATUS_CHECKED = 2;

    private long id;

    private String checkDate;// 检查日期

    @SerializedName("name")
    private String checkTableName;// 检查表名称

    private int status;// 状态
    private long companyId;// 企业id
    private long serviceId;// 服务id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckTableName() {
        return checkTableName;
    }

    public void setCheckTableName(String checkTableName) {
        this.checkTableName = checkTableName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }
}
