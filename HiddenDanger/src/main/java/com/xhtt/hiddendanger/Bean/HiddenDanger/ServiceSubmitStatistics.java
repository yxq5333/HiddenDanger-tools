package com.xhtt.hiddendanger.Bean.HiddenDanger;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hollow Goods on 2019-05-23.
 */
public class ServiceSubmitStatistics {

    @SerializedName("newRiskCnt")
    private int newHiddenDangerCount;// 新增隐患

    @SerializedName("reviewRiskCnt")
    private int changingCount;// 整改复查

    @SerializedName("rectifyRiskCnt")
    private int changedCount;// 整改完成

    public int getNewHiddenDangerCount() {
        return newHiddenDangerCount;
    }

    public void setNewHiddenDangerCount(int newHiddenDangerCount) {
        this.newHiddenDangerCount = newHiddenDangerCount;
    }

    public int getChangingCount() {
        return changingCount;
    }

    public void setChangingCount(int changingCount) {
        this.changingCount = changingCount;
    }

    public int getChangedCount() {
        return changedCount;
    }

    public void setChangedCount(int changedCount) {
        this.changedCount = changedCount;
    }
}
