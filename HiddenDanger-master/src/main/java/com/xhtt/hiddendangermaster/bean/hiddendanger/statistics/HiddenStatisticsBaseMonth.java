package com.xhtt.hiddendangermaster.bean.hiddendanger.statistics;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hollow Goods on 2019-04-16.
 */
public class HiddenStatisticsBaseMonth {

    private int month;

    @SerializedName("dangerFinishedMonth")
    private int hiddenDangerChanged;

    @SerializedName("dangerUnfinishedMonth")
    private int hiddenDangerUnchanged;

    @SerializedName("dangerTotalMonth")
    private int hiddenDangerTotal;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
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
