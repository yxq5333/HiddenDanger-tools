package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.common.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.net.ZRequestParamsBuilder;
import com.hg.zero.net.ZxUtils3;
import com.hg.zero.net.callback.base.ZRequestDataListener;
import com.hg.zero.toast.Zt;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.CommonChooseItem;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.common.contract.AreaContract;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Map;

/**
 * 省市区镇数据层
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaModel implements AreaContract.Model {

    private AreaContract.View mView;
    private Context mContext;

    public AreaModel(AreaContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
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

        RequestParams params = ZRequestParamsBuilder.buildKeyValueRequestParam(
                HttpMethod.GET,
                ZRequestParamsBuilder.buildRequestUrl(InterfaceApi.GetArea.getUrl()),
                null,
                request
        );
        params.addHeader("token", MyApplication.createApplication().getToken());

        new ZxUtils3.RequestDataBuilder().setRequestDataListener(new ZRequestDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<CommonChooseItem> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getData()),
                                    new TypeToken<ArrayList<CommonChooseItem>>() {
                                    }.getType()
                            );

                            mView.getDataSuccess(tempData);
                        }).start();
                    } else {
                        if (responseInfo.getCode() == ResponseInfo.CODE_FAIL) {
                            mView.getDataError(responseInfo.getMsg());
                        } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                            Zt.error("授权已过期，请重新登录");
                            ZEvent event = new ZEvent(EventActionCode.TokenOverdue);
                            EventBus.getDefault().post(event);
                        } else {
                            mView.getDataError(R.string.network_error);
                        }

                        mView.getDataFinish();
                    }
                }
            }

            @Override
            public void onGetError(Throwable ex) {
                if (isViewAttached()) {
                    mView.getDataError(R.string.network_error);
                    mView.getDataFinish();
                }
            }

            @Override
            public void onGetFinish() {

            }
        }).requestData(params);
    }
}
