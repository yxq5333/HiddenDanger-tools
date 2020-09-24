package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

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

    public HiddenDangerDetailRequest(Object serviceId, Object id, Object companyId, Object checkDate, Object riskAddress, Object riskPhotoList, Object riskDescription, Object riskLevel, Object referenceFrame, Object measure, Object rectifyPhotoList, Object memo, Object status, Object companyName, Object checkItemId, Object currServiceId) {
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
    }
}
