package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import com.google.gson.Gson;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.util.StringUtils;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.hg.hollowgoods.util.xutils.XUtils2;
import com.hg.hollowgoods.util.xutils.callback.base.GetHttpDataListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CompanyDetailRequest;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.math.BigDecimal;
import java.util.List;

/**
 * 企业基本信息数据层
 *
 * @author HG
 */

public class CompanyDetailModel implements CompanyDetailContract.Model {

    private CompanyDetailContract.View mView;

    public CompanyDetailModel(CompanyDetailContract.View mView) {
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
    public void addData(CompanyDetailRequest request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.CompanyAdd.getUrl()));
        params.setMethod(HttpMethod.POST);
        params.addHeader("token", MyApplication.createApplication().getToken());
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(request));

        new XUtils2.BuilderGetHttpData().setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        List<String> str = StringUtils.getStringArray(responseInfo.getData() + "", ",");
                        long id = new BigDecimal(str.get(0)).longValue();
                        long serviceId = new BigDecimal(str.get(1)).longValue();
                        mView.submitDataSuccess(id, serviceId);
                    } else {
                        if (responseInfo.getCode() == ResponseInfo.CODE_FAIL) {
                            t.error(responseInfo.getMsg());
                        } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                            t.error("授权已过期，请重新登录");
                            HGEvent event = new HGEvent(EventActionCode.TokenOverdue);
                            EventBus.getDefault().post(event);
                        } else {
                            t.error(R.string.network_error);
                        }

                        mView.submitDataError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
                    mView.submitDataError();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {
                    mView.submitDataFinish();
                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        }).getHttpData(params);
    }

    @Override
    public void editData(CompanyDetailRequest request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.CompanyEdit.getUrl()));
        params.setMethod(HttpMethod.POST);
        params.addHeader("token", MyApplication.createApplication().getToken());
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(request));

        new XUtils2.BuilderGetHttpData().setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        mView.submitDataSuccess(null, null);
                    } else {
                        if (responseInfo.getCode() == ResponseInfo.CODE_FAIL) {
                            t.error(responseInfo.getMsg());
                        } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                            t.error("授权已过期，请重新登录");
                            HGEvent event = new HGEvent(EventActionCode.TokenOverdue);
                            EventBus.getDefault().post(event);
                        } else {
                            t.error(R.string.network_error);
                        }

                        mView.submitDataError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
                    mView.submitDataError();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {
                    mView.submitDataFinish();
                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        }).getHttpData(params);
    }
}
