package com.xhtt.hiddendangermaster.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.hg.zero.constant.ZConstants;
import com.hg.zero.net.ZxUtils3;
import com.hg.zero.net.callback.base.ZRequestDataListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
import com.hg.zero.ui.base.ZBaseActivity;
import com.hg.zero.util.ZAppUtils;
import com.hg.zero.util.updateapp.ZUpdateAPPUtils;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.AppVersion;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.constant.AppStyle;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.constant.SystemConfig;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

/**
 * APP版本更新工具类
 * Created by HG on 2016-11-28.
 */

public class UpdateAPPUtils extends ZUpdateAPPUtils {

    public UpdateAPPUtils(ZBaseActivity baseActivity) {
        super(baseActivity);
        setDownloadApkMethod(HttpMethod.GET);
    }

    @Override
    public void checkServerData() {

        RequestParams params = new RequestParams(ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.UpdateVersion.getUrl()));
        params.setMethod(HttpMethod.GET);

        new ZxUtils3.RequestDataBuilder().setRequestDataListener(new ZRequestDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                    AppVersion appVersion = new Gson().fromJson(
                            new Gson().toJson(responseInfo.getData()),
                            AppVersion.class
                    );

                    if (!TextUtils.isEmpty(appVersion.getVersion()) && appVersion.getVersion().compareTo(ZAppUtils.getAppVersionName(getBaseActivity())) > 0) {
                        setURL(appVersion.getPackageUrl());

                        StringBuilder tip = new StringBuilder();
                        tip.append("V");
                        tip.append(ZAppUtils.getAppVersionName(getBaseActivity()));
                        tip.append(" → ");
                        tip.append("V");
                        tip.append(appVersion.getVersion());
                        tip.append("<br><br>");
                        tip.append("发布时间：");
                        tip.append("<br>");
                        tip.append(appVersion.getUpdateTime());
                        tip.append("<br><br>");
                        tip.append("更新内容：");
                        tip.append("<br>");
                        tip.append(appVersion.getContent());

                        showDialog(tip.toString());
                    } else {
                        if (isFromUser()) {
                            Zt.info(R.string.update_app_already_new);
                        }
                    }
                } else {
                    if (isFromUser()) {
                        Zt.info(R.string.update_app_already_new);
                    }
                }
            }

            @Override
            public void onGetError(Throwable ex) {
                if (isFromUser()) {
                    Zt.error(R.string.network_error);
                }
            }

            @Override
            public void onGetLoading(long total, long current) {

            }

            @Override
            public void onGetFinish() {
                if (isFromUser()) {
                    getBaseActivity().baseUI.baseDialog.closeDialog(ZConstants.UPDATE_APP_UTILS_CHECK_PROGRESS_DIALOG_CODE);
                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException cex) {

            }
        }).requestData(params);
    }

    @Override
    protected boolean isOpenDevelopmentVersionCheck() {
        return SystemConfig.APP_STYLE == AppStyle.XHTT_Test;
    }

    @Override
    protected String setPgyApiKey() {
        return "af523e39380fd3d004af76c62fde7b08";
    }

    @Override
    protected String setPgyAppKey() {
        return "b9efb515df0b5807fadad507522f34c7";
    }
}
