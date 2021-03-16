package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

import com.google.gson.annotations.SerializedName;
import com.hg.zero.bean.ZCommonBean;
import com.hg.zero.file.ZAppFile;
import com.xhtt.hiddendangermaster.util.uploadfile.WebFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class HiddenDanger extends ZCommonBean<HiddenDanger> {

    /**** 一般隐患 ****/
    public static final int HIDDEN_DANGER_LEVEL_NORMAL = 2;
    /**** 重大隐患 ****/
    public static final int HIDDEN_DANGER_LEVEL_BIG = 1;
    /**** 未整改 ****/
    public static final int STATUS_UNCHANGED = 0;
    /**** 已整改 ****/
    public static final int STATUS_CHANGED = 1;

    public HiddenDanger() {
        super(-1);
    }

    public HiddenDanger(int itemType) {
        super(itemType);
    }

    private long id;

    private long companyId;// 企业id

    @SerializedName("memo")
    private String changeDescribe;// 整改说明

    private String checkDateShow;

    @SerializedName("riskAddress")
    private String hiddenLocation;

    private String hiddenPhoto;

    @SerializedName("riskDescription")
    private String hiddenDescribe;

    private String hiddenLevel;

    @SerializedName("referenceFrame")
    private String reference;

    @SerializedName("measure")
    private String changeFunction;

    private String checkDate;// 检查日期

    @SerializedName("riskLevel")
    private Integer level;// 隐患等级

    private Integer status;// 状态

    @SerializedName("riskPhotoList")
    private ArrayList<WebFile> hiddenPhotoList;// 隐患照片

    @SerializedName("rectifyPhotoList")
    private ArrayList<WebFile> changePhotoList;// 整改后照片

    private List<ZAppFile> appHiddenPhotoList;

    private List<ZAppFile> appChangePhotoList;

    public String getChangeDescribe() {
        return changeDescribe;
    }

    private Long serviceId;// 服务记录id
    private int times;// 服务次数
    private int useTimes;// 引用次数

    @SerializedName("rectifyTime")
    private String changeDate;// 整改日期

    @SerializedName("userName")
    private String checkPeople;// 检查人

    @SerializedName("dangerLat")
    private String typeFirst;// 隐患大类

    @SerializedName("categorySub")
    private String typeSecond;// 细分类型

    @SerializedName("departRect")
    private String changeDepartment;// 整改部门

    @SerializedName("personLia")
    private String dutyPeople;// 责任人

    public String getTypeFirst() {
        return typeFirst;
    }

    public HiddenDanger setTypeFirst(String typeFirst) {
        this.typeFirst = typeFirst;
        return this;
    }

    public String getTypeSecond() {
        return typeSecond;
    }

    public HiddenDanger setTypeSecond(String typeSecond) {
        this.typeSecond = typeSecond;
        return this;
    }

    public String getChangeDepartment() {
        return changeDepartment;
    }

    public HiddenDanger setChangeDepartment(String changeDepartment) {
        this.changeDepartment = changeDepartment;
        return this;
    }

    public String getDutyPeople() {
        return dutyPeople;
    }

    public HiddenDanger setDutyPeople(String dutyPeople) {
        this.dutyPeople = dutyPeople;
        return this;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public void setChangeDescribe(String changeDescribe) {
        this.changeDescribe = changeDescribe;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<WebFile> getHiddenPhotoList() {
        return hiddenPhotoList;
    }

    public void setHiddenPhotoList(ArrayList<WebFile> hiddenPhotoList) {
        this.hiddenPhotoList = hiddenPhotoList;
    }

    public ArrayList<WebFile> getChangePhotoList() {
        return changePhotoList;
    }

    public void setChangePhotoList(ArrayList<WebFile> changePhotoList) {
        this.changePhotoList = changePhotoList;
    }

    public String getCheckDateShow() {
        return checkDateShow;
    }

    public void setCheckDateShow(String checkDateShow) {
        this.checkDateShow = checkDateShow;
    }

    public String getHiddenDescribe() {
        return hiddenDescribe;
    }

    public void setHiddenDescribe(String hiddenDescribe) {
        this.hiddenDescribe = hiddenDescribe;
    }

    public String getHiddenLocation() {
        return hiddenLocation;
    }

    public void setHiddenLocation(String hiddenLocation) {
        this.hiddenLocation = hiddenLocation;
    }

    public String getHiddenLevel() {
        return hiddenLevel;
    }

    public void setHiddenLevel(String hiddenLevel) {
        this.hiddenLevel = hiddenLevel;
    }

    public String getHiddenPhoto() {
        return hiddenPhoto;
    }

    public void setHiddenPhoto(String hiddenPhoto) {
        this.hiddenPhoto = hiddenPhoto;
    }

    public String getChangeFunction() {
        return changeFunction;
    }

    public void setChangeFunction(String changeFunction) {
        this.changeFunction = changeFunction;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(int useTimes) {
        this.useTimes = useTimes;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getCheckPeople() {
        return checkPeople;
    }

    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }

    public List<ZAppFile> getAppHiddenPhotoList() {
        return appHiddenPhotoList;
    }

    public void setAppHiddenPhotoList(List<ZAppFile> appHiddenPhotoList) {
        this.appHiddenPhotoList = appHiddenPhotoList;
    }

    public List<ZAppFile> getAppChangePhotoList() {
        return appChangePhotoList;
    }

    public void setAppChangePhotoList(List<ZAppFile> appChangePhotoList) {
        this.appChangePhotoList = appChangePhotoList;
    }
}
