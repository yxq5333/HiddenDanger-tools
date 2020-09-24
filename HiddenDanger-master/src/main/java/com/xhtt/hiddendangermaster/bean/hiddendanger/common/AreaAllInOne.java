package com.xhtt.hiddendangermaster.bean.hiddendanger.common;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 省市区镇整合
 * <p>
 * Created by Hollow Goods on 2020-04-09.
 */
public class AreaAllInOne {

    private ArrayList<CommonChooseItem> provinces;

    @SerializedName("citys")
    private ArrayList<CommonChooseItem> cities;

    @SerializedName("areas")
    private ArrayList<CommonChooseItem> districts;

    @SerializedName("streets")
    private ArrayList<CommonChooseItem> towns;

    public ArrayList<CommonChooseItem> getProvinces() {
        return provinces;
    }

    public void setProvinces(ArrayList<CommonChooseItem> provinces) {
        this.provinces = provinces;
    }

    public ArrayList<CommonChooseItem> getCities() {
        return cities;
    }

    public void setCities(ArrayList<CommonChooseItem> cities) {
        this.cities = cities;
    }

    public ArrayList<CommonChooseItem> getDistricts() {
        return districts;
    }

    public void setDistricts(ArrayList<CommonChooseItem> districts) {
        this.districts = districts;
    }

    public ArrayList<CommonChooseItem> getTowns() {
        return towns;
    }

    public void setTowns(ArrayList<CommonChooseItem> towns) {
        this.towns = towns;
    }
}
