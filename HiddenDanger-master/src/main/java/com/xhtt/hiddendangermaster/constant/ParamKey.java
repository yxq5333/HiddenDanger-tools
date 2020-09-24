package com.xhtt.hiddendangermaster.constant;

/**
 * 键
 * <p>
 * Created by Hollow Goods on 2020-09-11.
 */
public enum ParamKey {

    Token,// token
    Username,// 用户名
    Password,// 密码

    LawType,
    BackData,
    Status,
    Location,
    ParentData,
    BannerType,
    StringData,
    URL,

    GrandData,
    WorkType,
    IsOnlySelector,
    FromClass,
    Company,
    Province,
    City,
    District,
    Town,
    LastServiceId,
    SignData,
    HiddenDangerAddWithOutContinue,

    //
    ;

    @Override
    public String toString() {
        return "param.key." + name();
    }
}
