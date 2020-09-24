package com.xhtt.hiddendangermaster.application;

import android.app.Application;

import com.hg.hollowgoods.application.BaseApplication;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.util.ip.IPConfig;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.xhtt.hiddendangermaster.BuildConfig;
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
public class MyApplication extends BaseApplication {

    @Override
    public Application initAppContext() {
        return this;
    }

    @Override
    public String setRootDirectory() {
        return "XHTT";
    }

    @Override
    public boolean isNeedReadOfficeFile() {
        return true;
    }

    @Override
    public void initAppData() {

        EventBus.getDefault().register(this);

        SystemConfig.APP_STYLE = AppStyle.valueOf(MetaDataUtils.getMetaData(this, BuildConfig.MetaDataKeyChannel));
        String defaultIP = MetaDataUtils.getMetaData(this, BuildConfig.MetaDataKeyIP);
        Integer defaultPort = MetaDataUtils.getMetaData(this, BuildConfig.MetaDataKeyPORT);

        IPConfigHelper.create().initIPConfigs(new IPConfig().setIp(defaultIP).setPort(String.valueOf(defaultPort)));

        LoginUtils.initUser();
    }

    public static MyApplication createApplication() {
        return create();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUI(HGEvent event) {
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
