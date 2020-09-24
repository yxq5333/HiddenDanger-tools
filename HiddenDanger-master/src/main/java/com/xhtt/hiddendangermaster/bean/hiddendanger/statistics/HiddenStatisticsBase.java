package com.xhtt.hiddendangermaster.bean.hiddendanger.statistics;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Hollow Goods on 2019-04-16.
 */
public class HiddenStatisticsBase {

    @SerializedName("dangerFinished")
    private int hiddenDangerChanged;

    @SerializedName("dangerUnfinished")
    private int hiddenDangerUnchanged;

    @SerializedName("dangerTotal")
    private int hiddenDangerTotal;

    @SerializedName("yearRiskCnt")
    private int searchYearHiddenDangerCount;

    private ArrayList<HiddenStatisticsBaseMonth> list;

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

    public int getSearchYearHiddenDangerCount() {
        return searchYearHiddenDangerCount;
    }

    public void setSearchYearHiddenDangerCount(int searchYearHiddenDangerCount) {
        this.searchYearHiddenDangerCount = searchYearHiddenDangerCount;
    }

    public ArrayList<HiddenStatisticsBaseMonth> getList() {
        return list;
    }

    public void setList(ArrayList<HiddenStatisticsBaseMonth> list) {
        this.list = list;
    }
}
