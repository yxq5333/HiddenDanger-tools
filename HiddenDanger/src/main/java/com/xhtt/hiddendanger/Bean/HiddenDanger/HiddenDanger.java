package com.xhtt.hiddendanger.Bean.HiddenDanger;

import com.google.gson.annotations.SerializedName;
import com.hg.hollowgoods.Adapter.FastAdapter.Annotation.Item.FastItem;
import com.hg.hollowgoods.Adapter.FastAdapter.Annotation.Item.FastItemFileMaxCount;
import com.hg.hollowgoods.Adapter.FastAdapter.Constant.FastItemMode;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;
import com.hg.hollowgoods.Util.StringUtils;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Util.UploadFile.WebFile;

import java.util.ArrayList;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class HiddenDanger extends CommonBean {

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

    @FastItem(sortNumber = 10, label = "检查日期", isNotEmpty = true, mode = FastItemMode.Choose, isDate = true, dateFormatMode = StringUtils.DateFormatMode.LINE_YMD)
    private String checkDateShow;

    @FastItem(sortNumber = 20, label = "隐患地点", contentHint = "请输入")
    @SerializedName("riskAddress")
    private String hiddenLocation;

    @FastItem(sortNumber = 25, label = "隐患照片", mode = FastItemMode.File, rightIconName = "TAKE_PHOTO", rightIconNameClass = Constants.class)
    @FastItemFileMaxCount(maxCount = SystemConfig.FILE_MAX_COUNT)
    private String hiddenPhoto;

    @FastItem(sortNumber = 30, label = "隐患描述", isNotEmpty = true, contentHint = "请输入或选择", rightIconName = "HIDDEN_BASE", rightIconNameClass = Constants.class)
    @SerializedName("riskDescription")
    private String hiddenDescribe;

    @FastItem(sortNumber = 40, label = "隐患等级", contentHint = "请选择", mode = FastItemMode.Choose)
    private String hiddenLevel;

    @FastItem(sortNumber = 50, label = "参考依据", contentHint = "请输入")
    @SerializedName("referenceFrame")
    private String reference;

    @FastItem(sortNumber = 60, label = "整改措施", contentHint = "请输入")
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

}
