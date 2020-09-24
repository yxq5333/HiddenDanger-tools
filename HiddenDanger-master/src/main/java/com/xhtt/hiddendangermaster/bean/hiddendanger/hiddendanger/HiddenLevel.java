package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hollow Goods on 2019-10-30.
 */
public class HiddenLevel {

    @SerializedName("value")
    private int levelId;
    @SerializedName("label")
    private String levelName;

    public HiddenLevel() {
    }

    public HiddenLevel(int levelId, String levelName) {
        this.levelId = levelId;
        this.levelName = levelName;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
