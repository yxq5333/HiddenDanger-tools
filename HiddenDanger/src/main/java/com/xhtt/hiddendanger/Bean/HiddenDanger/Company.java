package com.xhtt.hiddendanger.Bean.HiddenDanger;

import com.google.gson.annotations.SerializedName;
import com.hg.hollowgoods.Adapter.FastAdapter.Annotation.Item.FastItem;
import com.hg.hollowgoods.Adapter.FastAdapter.Constant.FastItemMode;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class Company extends CommonBean {

    private long id;

    @FastItem(sortNumber = 10, label = "企业名称", isNotEmpty = true, contentHint = "请选择", mode = FastItemMode.Choose)
    @SerializedName("name")
    private String companyName;

    @FastItem(sortNumber = 15, label = "行政区域", contentHint = "请输入", mode = FastItemMode.Choose, visible = "isShowOther")
    private String manageArea;

    @FastItem(sortNumber = 20, label = "单位地址", contentHint = "请输入", visible = "isShowOther")
    private String address;

    @FastItem(sortNumber = 30, label = "主要联系人", contentHint = "请输入", visible = "isShowOther")
    @SerializedName("contact")
    private String mainPeople;

    @FastItem(sortNumber = 40, label = "联系电话", contentHint = "请输入", visible = "isShowOther")
    @SerializedName("phone")
    private String mainPeoplePhone;

    @FastItem(sortNumber = 50, label = "行业", contentHint = "请选择", mode = FastItemMode.Choose, visible = "isShowOther")
    @SerializedName("industry")
    private String business;

    @FastItem(sortNumber = 60, label = "规模情况", contentHint = "请选择", mode = FastItemMode.Choose, visible = "isShowOther")
    @SerializedName("scale")
    private String proportion;

    @FastItem(sortNumber = 70, label = "人员数量(人)", contentHint = "请输入", visible = "isShowOther")
    @SerializedName("userCount")
    private String peopleCount;

    @SerializedName("dangerTotal")
    private int hiddenDangerTotal;

    @SerializedName("dangerFinished")
    private int hiddenDangerChangeCount;

    @SerializedName("dangerUnfinished")
    private int hiddenDangerNoChangeCount;

    private long serviceId;// 服务记录id
    private int times;// 服务次数

    private boolean isShowOther = true;

    private Long checkItemId;// 检查下id
    private boolean isHiddenDangerOnce = false;

    private Long provinceId;

    private Long cityId;

    @SerializedName("areaId")
    private Long districtId;

    @SerializedName("streetId")
    private Long townId;

    public Company() {
        super(-1);
    }

    public Company(int itemType) {
        super(itemType);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManageArea() {
        return manageArea;
    }

    public void setManageArea(String manageArea) {
        this.manageArea = manageArea;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMainPeople() {
        return mainPeople;
    }

    public void setMainPeople(String mainPeople) {
        this.mainPeople = mainPeople;
    }

    public String getMainPeoplePhone() {
        return mainPeoplePhone;
    }

    public void setMainPeoplePhone(String mainPeoplePhone) {
        this.mainPeoplePhone = mainPeoplePhone;
    }

    public int getHiddenDangerTotal() {
        return hiddenDangerTotal;
    }

    public void setHiddenDangerTotal(int hiddenDangerTotal) {
        this.hiddenDangerTotal = hiddenDangerTotal;
    }

    public int getHiddenDangerChangeCount() {
        return hiddenDangerChangeCount;
    }

    public void setHiddenDangerChangeCount(int hiddenDangerChangeCount) {
        this.hiddenDangerChangeCount = hiddenDangerChangeCount;
    }

    public int getHiddenDangerNoChangeCount() {
        return hiddenDangerNoChangeCount;
    }

    public void setHiddenDangerNoChangeCount(int hiddenDangerNoChangeCount) {
        this.hiddenDangerNoChangeCount = hiddenDangerNoChangeCount;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public boolean isShowOther() {
        return isShowOther;
    }

    public void setShowOther(boolean showOther) {
        isShowOther = showOther;
    }

    public Long getCheckItemId() {
        return checkItemId;
    }

    public void setCheckItemId(Long checkItemId) {
        this.checkItemId = checkItemId;
    }

    public boolean isHiddenDangerOnce() {
        return isHiddenDangerOnce;
    }

    public void setHiddenDangerOnce(boolean hiddenDangerOnce) {
        isHiddenDangerOnce = hiddenDangerOnce;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getTownId() {
        return townId;
    }

    public void setTownId(Long townId) {
        this.townId = townId;
    }
}
