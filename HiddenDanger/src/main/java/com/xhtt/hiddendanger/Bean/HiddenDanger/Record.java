package com.xhtt.hiddendanger.Bean.HiddenDanger;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class Record implements Serializable {

    private long serviceId;// 服务id
    private long companyId;// 企业id

    private String checkDate;// 检查日期

    @SerializedName("rectifyTime")
    private String changeDate;// 整改期限

    @SerializedName("serviceTimes")
    private int times;// 服务次数

    @SerializedName("dangerTotal")
    private int hiddenDangerTotal;// 隐患总数

    @SerializedName("dangerFinished")
    private int hiddenDangerChangedCount;// 整改数

    @SerializedName("dangerUnfinished")
    private int hiddenDangerUnchangedCount;// 未整改数

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getHiddenDangerTotal() {
        return hiddenDangerTotal;
    }

    public void setHiddenDangerTotal(int hiddenDangerTotal) {
        this.hiddenDangerTotal = hiddenDangerTotal;
    }

    public int getHiddenDangerChangedCount() {
        return hiddenDangerChangedCount;
    }

    public void setHiddenDangerChangedCount(int hiddenDangerChangedCount) {
        this.hiddenDangerChangedCount = hiddenDangerChangedCount;
    }

    public int getHiddenDangerUnchangedCount() {
        return hiddenDangerUnchangedCount;
    }

    public void setHiddenDangerUnchangedCount(int hiddenDangerUnchangedCount) {
        this.hiddenDangerUnchangedCount = hiddenDangerUnchangedCount;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }
}
