package com.xhtt.hiddendangermaster.application;

import android.app.Application;

import com.hg.zero.application.ZBaseApplication;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZSystemConfig;
import com.hg.zero.constant.ZConstants;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfig;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
import com.xhtt.hiddendangermaster.BuildConfig;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.ListLimitDataInfo;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.constant.AppStyle;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.util.LoginUtils;
import com.xhtt.hiddendangermaster.util.MetaDataUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Hollow Goods on 2019-03-26.
 */
public class MyApplication extends ZBaseApplication {

    @Override
    public Application initAppContext() {
        return this;
    }

    @Override
    public String setRootDirectory() {
        return "XHTT";
    }

    @Override
    public void initTopData() {
        ZSystemConfig.setOpenOfficeFileReader(true);
    }

    @Override
    public void initAppData() {

        EventBus.getDefault().register(this);

        SystemConfig.APP_STYLE = AppStyle.valueOf(MetaDataUtils.getMetaData(this, BuildConfig.MetaDataKeyChannel));
        String defaultIP = MetaDataUtils.getMetaData(this, BuildConfig.MetaDataKeyIP);
        Integer defaultPort = MetaDataUtils.getMetaData(this, BuildConfig.MetaDataKeyPORT);

        ZIPConfig ipConfig = new ZIPConfig();
        ipConfig.setIp(defaultIP);
        ipConfig.setPort(String.valueOf(defaultPort));

        ZIPConfigHelper.get().initIPConfigs(ipConfig);

        ZSystemConfig.setUsername(getString(R.string.app_name));

        LoginUtils.initUser();

        ZSystemConfig.bindResponseClass(ResponseInfo.class, ListLimitDataInfo.class);
        ZSystemConfig.setSingleChoiceDialogAutoBack(true);
        ZSystemConfig.setActivityStartInAnim(ZConstants.ACTIVITY_ANIMATION[6][0]);
        ZSystemConfig.setActivityStartOutAnim(ZConstants.ACTIVITY_ANIMATION[6][1]);
        ZSystemConfig.setActivityFinishInAnim(0);
        ZSystemConfig.setActivityFinishOutAnim(ZConstants.ACTIVITY_ANIMATION[1][1]);
    }

    public static MyApplication createApplication() {
        return create();
    }

    @Override
    public void tokenOverDate() {
        LoginUtils.autoExitApp(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUI(ZEvent event) {
        if (event.getEventActionCode() == EventActionCode.TokenOverdue) {
            // Token过期
            LoginUtils.autoExitApp(this);
        }
    }

    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
