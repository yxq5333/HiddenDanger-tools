package com.xhtt.hiddendangermaster.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.hg.hollowgoods.constant.HGConstants;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.util.APPUtils;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.hg.hollowgoods.util.updateapp.HGUpdateAPPUtils;
import com.hg.hollowgoods.util.xutils.XUtils2;
import com.hg.hollowgoods.util.xutils.callback.base.GetHttpDataListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.AppVersion;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

/**
 * APP版本更新工具类
 * Created by HG on 2016-11-28.
 */

public class UpdateAPPUtils extends HGUpdateAPPUtils {

    public UpdateAPPUtils(BaseActivity baseActivity) {
        super(baseActivity);
        setDownloadApkMethod(HttpMethod.GET);
    }

    @Override
    public void checkServerData() {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.UpdateVersion.getUrl()));
        params.setMethod(HttpMethod.GET);

        new XUtils2.BuilderGetHttpData().setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                    AppVersion appVersion = new Gson().fromJson(
                            new Gson().toJson(responseInfo.getData()),
                            AppVersion.class
                    );

                    if (!TextUtils.isEmpty(appVersion.getVersion()) && appVersion.getVersion().compareTo(APPUtils.getAppVersionName(getBaseActivity())) > 0) {
                        setURL(appVersion.getPackageUrl());

                        StringBuilder tip = new StringBuilder();
                        tip.append("V");
                        tip.append(APPUtils.getAppVersionName(getBaseActivity()));
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
                            t.info(R.string.update_app_already_new);
                        }
                    }
                } else {
                    if (isFromUser()) {
                        t.info(R.string.update_app_already_new);
                    }
                }
            }

            @Override
            public void onGetError(Throwable ex) {
                if (isFromUser()) {
                    t.error(R.string.network_error);
                }
            }

            @Override
            public void onGetLoading(long total, long current) {

            }

            @Override
            public void onGetFinish() {
                if (isFromUser()) {
                    getBaseActivity().baseUI.baseDialog.closeDialog(HGConstants.UPDATE_APP_UTILS_CHECK_PROGRESS_DIALOG_CODE);
                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException cex) {

            }
        }).getHttpData(params);
    }
}
