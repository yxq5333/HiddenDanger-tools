package com.xhtt.hiddendangermaster.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 企业地图
 * <p>
 * Created by Hollow Goods on 2020-04-02.
 */
public class CompanyMap {

    @SerializedName("id")
    private long companyId;// 企业id
    private String lat;// 纬度
    private String lng;// 经度
    private String companyName;// 企业名称
    private String companyAddress;// 企业地址

    @SerializedName("companyContacts")
    private String companyContactsName;// 企业联系人

    private String companyContactsPhone;// 企业联系人联系方式

    @SerializedName("serviceTimes")
    private int serviceNowCount;// 当前服务次数

    private int serviceTotalCount;// 总服务次数

    // Getter

    public long getCompanyId() {
        return companyId;
    }

    public Double getLat() {
        if (TextUtils.isEmpty(lat)) {
            return null;
        }
        return Double.valueOf(lat);
    }

    public Double getLng() {
        if (TextUtils.isEmpty(lng)) {
            return null;
        }
        return Double.valueOf(lng);
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public int getServiceNowCount() {
        return serviceNowCount;
    }

    public int getServiceTotalCount() {
        return serviceTotalCount;
    }

    public String getCompanyContactsName() {
        return companyContactsName;
    }

    public String getCompanyContactsPhone() {
        return companyContactsPhone;
    }

    // Setter


    public CompanyMap setCompanyContactsName(String companyContactsName) {
        this.companyContactsName = companyContactsName;
        return this;
    }

    public CompanyMap setCompanyContactsPhone(String companyContactsPhone) {
        this.companyContactsPhone = companyContactsPhone;
        return this;
    }

    public CompanyMap setCompanyId(long companyId) {
        this.companyId = companyId;
        return this;
    }

    public CompanyMap setLat(String lat) {
        this.lat = lat;
        return this;
    }

    public CompanyMap setLng(String lng) {
        this.lng = lng;
        return this;
    }

    public CompanyMap setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public CompanyMap setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
        return this;
    }

    public CompanyMap setServiceNowCount(int serviceNowCount) {
        this.serviceNowCount = serviceNowCount;
        return this;
    }

    public CompanyMap setServiceTotalCount(int serviceTotalCount) {
        this.serviceTotalCount = serviceTotalCount;
        return this;
    }
}
