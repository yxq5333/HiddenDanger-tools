package com.xhtt.hiddendanger.Bean.Statistics;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Hollow Goods on 2019-04-16.
 */
public class ServiceCompanyBase {

    private int yearCompanyCnt;
    private int totalCnt;
    private int currentCnt;
    private ArrayList<Map<String, Integer>> list;

    public int getYearCompanyCnt() {
        return yearCompanyCnt;
    }

    public void setYearCompanyCnt(int yearCompanyCnt) {
        this.yearCompanyCnt = yearCompanyCnt;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getCurrentCnt() {
        return currentCnt;
    }

    public void setCurrentCnt(int currentCnt) {
        this.currentCnt = currentCnt;
    }

    public ArrayList<Map<String, Integer>> getList() {
        return list;
    }

    public void setList(ArrayList<Map<String, Integer>> list) {
        this.list = list;
    }
}
