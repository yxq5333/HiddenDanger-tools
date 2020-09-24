package com.xhtt.hiddendangermaster.constant;

/**
 * Created by Hollow Goods on 2019-04-03.
 */
public enum InterfaceApi {

    ShowFile("api-danger/file/show/"),
    DownloadFile("api-danger/file/down/"),
    UploadFile("api-danger/file/uploadList"),

    GetCompanyMapList("api-danger/insurance/company/allList"),
    AddServiceCount("api-danger/insurance/company/addServiceTimes"),
    Export("api-danger/insurance/company/exportExcel"),

    Register("api-danger/register"),
    Login("api-danger/appLogin"),
    GetUserData("api-danger/userInfo"),
    ChangePassword("api-danger/sys/user/password"),
    UpdateVersion("api-danger/appVersion/info"),
    UpdateUserData("api-danger/sys/user/appUpdate"),

    LawsList("api-danger/law/list"),
    TechnologyStandardList("api-danger/technical/list"),
    DangerProductList("api-danger/dangerous/list"),
    MSDSList("api-danger/msds/list"),
    AccidentCaseList("api-danger/accident/list"),
    AccidentCaseAddReadCount("api-danger/accident/updateViewCnt/"),
    BannerList("api-danger/adv/list"),

    CompanyAdd("api-danger/company/save"),
    CompanyEdit("api-danger/company/update"),
    CompanyDelete("api-danger/company/delete"),
    CompanyList("api-danger/company/list"),
    FindCompanyName("api-danger/company/searchCompanyList"),
    HiddenDangerAdd("api-danger/danger/save"),
    HiddenDangerEdit("api-danger/danger/update"),
    HiddenDangerSubmit("api-danger/danger/updateMeasure"),
    HiddenDangerDelete("api-danger/danger/delete"),
    HiddenDangerList("api-danger/danger/list"),
    HiddenDangerStoreList("api-danger/cfgdictdanger/listAll"),
    HiddenDangerStoreList2("api-danger/danger/listAll"),
    CheckTableList("api-danger/check-table/company/list"),
    CheckTableDelete("api-danger/check-table/company/delete"),
    CheckTableSubmit("api-danger/check-table/company/submit"),
    CheckTableContentList("api-danger/check-table/company/item/dataList"),
    ChangeCheckTableContentStatus("api-danger/check-table/company/item/update"),
    RecordList("api-danger/company/service/check/list"),
    ServiceSubmit("api-danger/company/service/submit"),
    ServiceSubmitStatistics("api-danger/company/service/riskStatics/"),
    StatisticsServiceCompanyBase("api-danger/company/staticsCompanyTime"),
    StatisticsServiceCompanyList("api-danger/company/staticsCompanyList"),
    HiddenDangerStatisticsBase("api-danger/danger/staticsRiskData"),
    HiddenDangerStatisticsList("api-danger/danger/staticsCompanyList"),
    HiddenDangerLevel("api-danger/check/cfgdict/getHiddenDangerLevel"),
    GetArea("api-danger/check/cfgarea/listByParentId"),
    GetAreaAllInOne("api-danger/check/cfgarea/selfDetailList"),

    GetPDF("api-danger/danger/exportWord"),

    //
    ;

    private String url;

    InterfaceApi(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
