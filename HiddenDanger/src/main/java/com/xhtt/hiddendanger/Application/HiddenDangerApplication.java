package com.xhtt.hiddendanger.Application;

import android.app.Application;

import com.hg.hollowgoods.Util.IP.IPConfig;

/**
 * 隐患排查模块专用单例
 * Created by Hollow Goods on 2019-03-26.
 */
public class HiddenDangerApplication extends Application {

    private static HiddenDangerApplication instance;

    public static HiddenDangerApplication create() {

        if (instance == null) {
            instance = new HiddenDangerApplication();
        }

        return instance;
    }

    private HiddenDangerApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //            private IPConfig ipConfig = new IPConfig().setIp("192.168.1.135").setPort("7006");
    private IPConfig ipConfig = new IPConfig().setIp("218.93.5.75").setPort("8022");
    private String token;

    public IPConfig getIpConfig() {
        return ipConfig;
    }

    public void setIpConfig(IPConfig ipConfig) {
        this.ipConfig = ipConfig;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
