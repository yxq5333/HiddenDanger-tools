package com.xhtt.hiddendanger.Bean.Statistics;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hollow Goods on 2019-04-10.
 */
public class ServiceCompany {

    @SerializedName("name")
    private String companyName;

    @SerializedName("currentTimes")
    private int nowYearTimes;

    @SerializedName("times")
    private int totalTimes;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getNowYearTimes() {
        return nowYearTimes;
    }

    public void setNowYearTimes(int nowYearTimes) {
        this.nowYearTimes = nowYearTimes;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }
}
