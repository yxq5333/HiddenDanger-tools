package com.xhtt.hiddendanger.Bean.HiddenDanger;

/**
 * Created by Hollow Goods on 2019-04-11.
 */
public class CompanyDetailRequest {

    private Object id;
    private Object name;// 企业名称
    private Object address;// 单位地址
    private Object contact;// 主要联系人
    private Object phone;// 联系电话
    private Object industry;// 行业
    private Object scale;// 规模情况
    private Object userCount;// 人员数量
    private Object provinceId;
    private Object cityId;
    private Object areaId;
    private Object streetId;

    public CompanyDetailRequest(Object id, Object name, Object address, Object contact, Object phone, Object industry, Object scale, Object userCount, Object provinceId, Object cityId, Object areaId, Object streetId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.phone = phone;
        this.industry = industry;
        this.scale = scale;
        this.userCount = userCount;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.areaId = areaId;
        this.streetId = streetId;
    }
}
