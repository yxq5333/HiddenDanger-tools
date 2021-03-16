package com.xhtt.hiddendangermaster.service;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.constant.ZParamKey;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by HG on 2018-03-19.
 */

public class JavaScriptInterface {

    private Context mContext;

    public JavaScriptInterface(Context mContext) {
        this.mContext = mContext;
    }

    @JavascriptInterface
    public void openFile(String title, String url) {
        ZEvent event = new ZEvent(EventActionCode.OPEN_WEB_FILE);
        event.addObj(ZParamKey.Title, title);
        event.addObj(ParamKey.URL, url);
        EventBus.getDefault().post(event);
    }

    @JavascriptInterface
    public void playVideo(String title, String url) {
        ZEvent event = new ZEvent(EventActionCode.PLAY_VIDEO);
        event.addObj(ZParamKey.Title, title);
        event.addObj(ParamKey.URL, url);
        EventBus.getDefault().post(event);
    }

}
