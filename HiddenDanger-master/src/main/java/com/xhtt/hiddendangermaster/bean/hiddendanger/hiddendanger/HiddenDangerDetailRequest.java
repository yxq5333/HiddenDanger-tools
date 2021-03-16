package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hollow Goods on 2019-04-11.
 */
public class HiddenDangerDetailRequest {

    private Object serviceId;// 服务记录id
    private Object id;
    private Object companyId;// 企业id
    private Object checkDate;// 检查日期
    private Object riskAddress;// 隐患地点
    private Object riskPhotoList;// 隐患照片
    private Object riskDescription;// 隐患描述
    private Object riskLevel;// 隐患等级
    private Object referenceFrame;// 参考依据
    private Object measure;// 整改措施
    private Object rectifyPhotoList;// 整改后照片
    private Object memo;// 整改说明
    private Object status;// 状态
    private Object companyName;// 企业名称
    private Object checkItemId;// 检查项id
    private Object currServiceId;// 最新的serviceId
    @SerializedName("dangerLat")
    private String typeFirst;// 隐患大类
    @SerializedName("categorySub")
    private String typeSecond;// 细分类型
    @SerializedName("departRect")
    private String changeDepartment;// 整改部门
    @SerializedName("personLia")
    private String dutyPeople;// 责任人

    public HiddenDangerDetailRequest(Object serviceId, Object id, Object companyId, Object checkDate, Object riskAddress, Object riskPhotoList, Object riskDescription, Object riskLevel, Object referenceFrame, Object measure, Object rectifyPhotoList, Object memo, Object status, Object companyName, Object checkItemId, Object currServiceId,
                                     String typeFirst,
                                     String typeSecond,
                                     String changeDepartment,
                                     String dutyPeople
    ) {
        this.serviceId = serviceId;
        this.id = id;
        this.companyId = companyId;
        this.checkDate = checkDate;
        this.riskAddress = riskAddress;
        this.riskPhotoList = riskPhotoList;
        this.riskDescription = riskDescription;
        this.riskLevel = riskLevel;
        this.referenceFrame = referenceFrame;
        this.measure = measure;
        this.rectifyPhotoList = rectifyPhotoList;
        this.memo = memo;
        this.status = status;
        this.companyName = companyName;
        this.checkItemId = checkItemId;
        this.currServiceId = currServiceId;
        this.typeFirst = typeFirst;
        this.typeSecond = typeSecond;
        this.changeDepartment = changeDepartment;
        this.dutyPeople = dutyPeople;
    }
}
