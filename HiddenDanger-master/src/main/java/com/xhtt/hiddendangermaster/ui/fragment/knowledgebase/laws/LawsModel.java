package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.laws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.net.ZxUtils3;
import com.hg.zero.net.callback.base.ZRequestDataListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.bean.knowledgebase.laws.Laws;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 法律法规数据层
 *
 * @author HG
 */

public class LawsModel implements LawsContract.Model {

    private LawsContract.View mView;

    public LawsModel(LawsContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void getData(Map<String, Object> request) {

        RequestParams params = new RequestParams(ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.LawsList.getUrl()));
        params.setMethod(HttpMethod.GET);
        params.addHeader("token", MyApplication.createApplication().getToken());

        Set<String> set = request.keySet();
        Iterator<String> keys = set.iterator();
        String key;
        Object value;

        while (keys.hasNext()) {
            key = keys.next();
            value = request.get(key);
            if (value != null) {
                params.addParameter(key, value);
            }
        }

        new ZxUtils3.RequestDataBuilder().setRequestDataListener(new ZRequestDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<Laws> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getPage().getList()),
                                    new TypeToken<ArrayList<Laws>>() {
                                    }.getType()
                            );

                            mView.getDataSuccess(tempData);
                        }).start();
                    } else {
                        if (responseInfo.getCode() == ResponseInfo.CODE_FAIL) {
                            Zt.error(responseInfo.getMsg());
                        } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                            Zt.error("授权已过期，请重新登录");
                            ZEvent event = new ZEvent(EventActionCode.TokenOverdue);
                            EventBus.getDefault().post(event);
                        } else {
                            Zt.error(R.string.network_error);
                        }

                        mView.getDataError();
                        mView.getDataFinish();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    Zt.error(R.string.network_error);
                    mView.getDataError();
                    mView.getDataFinish();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {

                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        }).requestData(params);
    }
}
