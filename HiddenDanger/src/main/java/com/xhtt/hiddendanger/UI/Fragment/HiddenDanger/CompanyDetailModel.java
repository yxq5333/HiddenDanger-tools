package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import com.google.gson.Gson;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.StringUtils;
import com.hg.hollowgoods.Util.XUtils.GetHttpDataListener;
import com.hg.hollowgoods.Util.XUtils.XUtils;
import com.xhtt.hiddendanger.Application.HiddenDangerApplication;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CompanyDetailRequest;
import com.xhtt.hiddendanger.Bean.ResponseInfo;
import com.xhtt.hiddendanger.Constant.InterfaceApi;
import com.xhtt.hiddendanger.R;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.math.BigDecimal;
import java.util.ArrayList;

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
        params.addHeader("token", MyApplication.createApplication().getToken());
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(request));

        new XUtils2.BuilderGetHttpData()
        xUtils.setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        ArrayList<String> str = StringUtils.getStringArray(responseInfo.getData() + "", ",");
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
        });
        xUtils.getHttpData(HttpMethod.POST, params);
    }

    @Override
    public void editData(CompanyDetailRequest request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.CompanyEdit.getUrl()));
        params.addHeader("token", MyApplication.createApplication().getToken());
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(request));

        new XUtils2.BuilderGetHttpData()
        xUtils.setGetHttpDataListener(new GetHttpDataListener() {
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
        });
        xUtils.getHttpData(HttpMethod.POST, params);
    }
}
