package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.util.StringUtils;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.hg.hollowgoods.util.xutils.XUtils2;
import com.hg.hollowgoods.util.xutils.callback.base.GetHttpDataListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.bean.ResponseInfoSpecial;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CheckTableContent;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenLevel;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.util.uploadfile.UploadFileUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 检查内容数据层
 *
 * @author HG
 */

public class CheckTableDetailModel implements CheckTableDetailContract.Model {

    private CheckTableDetailContract.View mView;

    public CheckTableDetailModel(CheckTableDetailContract.View mView) {
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

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.CheckTableContentList.getUrl()));
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

                ResponseInfoSpecial responseInfo = new Gson().fromJson(result, ResponseInfoSpecial.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<CheckTableContent> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getPage()),
                                    new TypeToken<ArrayList<CheckTableContent>>() {
                                    }.getType()
                            );

                            if (tempData != null) {
                                for (CheckTableContent t : tempData) {
                                    if (t.getHiddenDanger() != null) {
                                        t.getHiddenDanger().setCheckDateShow(StringUtils.getDateTimeLong(t.getHiddenDanger().getCheckDate(), StringUtils.DateFormatMode.LINE_YMD) + "");
                                        if (t.getHiddenDanger().getLevel() != null) {
                                            HiddenLevel hiddenLevel = null;

                                            for (HiddenLevel p : Constants.HIDDEN_LEVEL) {
                                                if (p.getLevelId() == t.getHiddenDanger().getLevel()) {
                                                    hiddenLevel = p;
                                                }
                                            }

                                            if (hiddenLevel == null) {
                                                t.getHiddenDanger().setHiddenLevel("");
                                            } else {
                                                t.getHiddenDanger().setHiddenLevel(hiddenLevel.getLevelName());
                                            }
                                        }
                                        if (t.getHiddenDanger().getHiddenPhotoList() != null) {
                                            t.getHiddenDanger().setAppHiddenPhotoList(UploadFileUtils.webFiles2AppFiles(t.getHiddenDanger().getHiddenPhotoList()));
                                        }
                                        if (t.getHiddenDanger().getChangePhotoList() != null) {
                                            t.getHiddenDanger().setAppChangePhotoList(UploadFileUtils.webFiles2AppFiles(t.getHiddenDanger().getChangePhotoList()));
                                        }
                                    }
                                }
                            }

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
    public void changeContentStatus(Map<String, Object> request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.ChangeCheckTableContentStatus.getUrl()));
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
                        mView.changeContentStatusSuccess();
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

    @Override
    public void submitData(Map<String, Object> request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.CheckTableSubmit.getUrl()));
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
                        mView.submitDataSuccess();
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