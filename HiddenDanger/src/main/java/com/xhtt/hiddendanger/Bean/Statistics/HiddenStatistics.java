package com.xhtt.hiddendanger.Bean.Statistics;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hollow Goods on 2019-04-10.
 */
public class HiddenStatistics {

    @SerializedName("name")
    private String companyName;

    @SerializedName("dangerFinished")
    private int hiddenDangerChanged;

    @SerializedName("dangerUnfinished")
    private int hiddenDangerUnchanged;

    @SerializedName("dangerTotal")
    private int hiddenDangerTotal;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getHiddenDangerChanged() {
        return hiddenDangerChanged;
    }

    public void setHiddenDangerChanged(int hiddenDangerChanged) {
        this.hiddenDangerChanged = hiddenDangerChanged;
    }

    public int getHiddenDangerUnchanged() {
        return hiddenDangerUnchanged;
    }

    public void setHiddenDangerUnchanged(int hiddenDangerUnchanged) {
        this.hiddenDangerUnchanged = hiddenDangerUnchanged;
    }

    public int getHiddenDangerTotal() {
        return hiddenDangerTotal;
    }

    public void setHiddenDangerTotal(int hiddenDangerTotal) {
        this.hiddenDangerTotal = hiddenDangerTotal;
    }
}
