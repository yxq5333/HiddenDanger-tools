package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.ui.base.message.dialog2.ChoiceItem;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.hg.hollowgoods.util.xutils.XUtils2;
import com.hg.hollowgoods.util.xutils.callback.base.GetHttpDataListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenLevel;
import com.xhtt.hiddendangermaster.constant.Constants;
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
 * 企业信息数据层
 *
 * @author HG
 */

public class CompanyListModel implements CompanyListContract.Model {

    private CompanyListContract.View mView;

    public CompanyListModel(CompanyListContract.View mView) {
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

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.CompanyList.getUrl()));
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

        new XUtils2.BuilderGetHttpData().setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<Company> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getPage().getList()),
                                    new TypeToken<ArrayList<Company>>() {
                                    }.getType()
                            );

                            mView.getDataSuccess(tempData);
                        }).start();
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

                        mView.getDataError();
                        mView.getDataFinish();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
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
        }).getHttpData(params);
    }

    @Override
    public void deleteData(ArrayList<Long> request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.CompanyDelete.getUrl()));
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
                        mView.deleteDataSuccess();
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

                        mView.deleteDataError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
                    mView.deleteDataError();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {
                    mView.deleteDataFinish();
                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        }).getHttpData(params);
    }

    @Override
    public void getHiddenLevel() {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.HiddenDangerLevel.getUrl()));
        params.setMethod(HttpMethod.POST);
        params.addHeader("token", MyApplication.createApplication().getToken());

        new XUtils2.BuilderGetHttpData().setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        ArrayList<HiddenLevel> tempData = new Gson().fromJson(
                                new Gson().toJson(responseInfo.getData()),
                                new TypeToken<ArrayList<HiddenLevel>>() {
                                }.getType()
                        );

                        if (tempData != null) {
                            Constants.HIDDEN_LEVEL.clear();
                            Constants.HIDDEN_LEVEL_OBJ.clear();

                            Constants.HIDDEN_LEVEL.addAll(tempData);

                            for (HiddenLevel t : tempData) {
                                Constants.HIDDEN_LEVEL_OBJ.add(new ChoiceItem(t.getLevelName()));
                            }
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
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
        }).getHttpData(params);
    }

}