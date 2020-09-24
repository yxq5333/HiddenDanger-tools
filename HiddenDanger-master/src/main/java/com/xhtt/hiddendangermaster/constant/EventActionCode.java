package com.xhtt.hiddendangermaster.constant;

/**
 * 意图代号
 * <p>
 * Created by Hollow Goods on 2019-04-24.
 */
public enum EventActionCode {

//    public static final int CLEAR_CACHE = 3000;

    TokenOverdue,// Token过期
    ClearCache,// 清理缓存

    UPLOAD_PHOTO,//= 2000;
    OPEN_WEB_FILE,// = 2001;
    PLAY_VIDEO,// = 2002;

    COMPANY_SELECTOR,// = 1001;
    COMPANY_SUBMIT,// = 1002;
    HIDDEN_DANGER_SUBMIT,// = 1003;
    SELECTOR_HIDDEN_DANGER_STORE,// = 1004;
    CHANGE_CHECK_TABLE_CONTENT_STATUS,// = 1005;
    CHECK_TABLE_SUBMIT,// = 1006;
    SERVICE_SUBMIT,// = 1007;
    CHECKED_AREA,// = 1008;
    SIGN_BACK,// = 1009;

    WE_CHAT_LOGIN_GET_CODE_SUCCESS,

}
