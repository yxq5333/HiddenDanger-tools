package com.xhtt.hiddendangermaster.service;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.constant.HGParamKey;
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
        HGEvent event = new HGEvent(EventActionCode.OPEN_WEB_FILE);
        event.addObj(HGParamKey.Title, title);
        event.addObj(ParamKey.URL, url);
        EventBus.getDefault().post(event);
    }

    @JavascriptInterface
    public void playVideo(String title, String url) {
        HGEvent event = new HGEvent(EventActionCode.PLAY_VIDEO);
        event.addObj(HGParamKey.Title, title);
        event.addObj(ParamKey.URL, url);
        EventBus.getDefault().post(event);
    }

}
