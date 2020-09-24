package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.StringUtils;
import com.hg.hollowgoods.Util.XUtils.GetHttpDataListener;
import com.hg.hollowgoods.Util.XUtils.XUtils;
import com.xhtt.hiddendanger.Application.HiddenDangerApplication;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenLevel;
import com.xhtt.hiddendanger.Bean.ResponseInfo;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Constant.InterfaceApi;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.Util.UploadFile.UploadFileUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 隐患列表数据层
 *
 * @author HG
 */

public class HiddenDangerListModel implements HiddenDangerListContract.Model {

    private HiddenDangerListContract.View mView;

    public HiddenDangerListModel(HiddenDangerListContract.View mView) {
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

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.HiddenDangerList.getUrl()));
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

        new XUtils2.BuilderGetHttpData()
        xUtils.setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<HiddenDanger> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getPage().getList()),
                                    new TypeToken<ArrayList<HiddenDanger>>() {
                                    }.getType()
                            );

                            if (tempData != null) {
                                for (HiddenDanger t : tempData) {
                                    t.setCheckDateShow(StringUtils.getDateLong(t.getCheckDate(), StringUtils.DateFormatMode.LINE_YMD) + "");
                                    if (t.getLevel() != null) {
                                        HiddenLevel hiddenLevel = null;

                                        for (HiddenLevel p : Constants.HIDDEN_LEVEL) {
                                            if (p.getLevelId() == t.getLevel()) {
                                                hiddenLevel = p;
                                            }
                                        }

                                        if (hiddenLevel == null) {
                                            t.setHiddenLevel("");
                                        } else {
                                            t.setHiddenLevel(hiddenLevel.getLevelName());
                                        }
                                    }
                                    if (t.getHiddenPhotoList() != null) {
                                        t.getMedia().put(25, UploadFileUtils.webFiles2AppFiles(t.getHiddenPhotoList()));
                                    }
                                    if (t.getChangePhotoList() != null) {
                                        t.getMedia().put(1, UploadFileUtils.webFiles2AppFiles(t.getChangePhotoList()));
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
        });
        xUtils.getHttpData(HttpMethod.GET, params);
    }

    @Override
    public void getStoreData(Map<String, Object> request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.HiddenDangerStoreList.getUrl()));
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

        new XUtils2.BuilderGetHttpData()
        xUtils.setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<HiddenDanger> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getPage().getList()),
                                    new TypeToken<ArrayList<HiddenDanger>>() {
                                    }.getType()
                            );

                            if (tempData != null) {
                                for (HiddenDanger t : tempData) {
                                    if (TextUtils.isEmpty(t.getCheckDate())) {
                                        t.setCheckDateShow("0");
                                    } else {
                                        t.setCheckDateShow(StringUtils.getDateLong(t.getCheckDate(), StringUtils.DateFormatMode.LINE_YMD) + "");
                                    }
                                    if (t.getLevel() != null) {
                                        HiddenLevel hiddenLevel = null;

                                        for (HiddenLevel p : Constants.HIDDEN_LEVEL) {
                                            if (p.getLevelId() == t.getLevel()) {
                                                hiddenLevel = p;
                                            }
                                        }

                                        if (hiddenLevel == null) {
                                            t.setHiddenLevel("");
                                        } else {
                                            t.setHiddenLevel(hiddenLevel.getLevelName());
                                        }
                                    }
                                    if (t.getHiddenPhotoList() != null) {
                                        t.getMedia().put(25, UploadFileUtils.webFiles2AppFiles(t.getHiddenPhotoList()));
                                    }
                                    if (t.getChangePhotoList() != null) {
                                        t.getMedia().put(1, UploadFileUtils.webFiles2AppFiles(t.getChangePhotoList()));
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
        });
        xUtils.getHttpData(HttpMethod.GET, params);
    }

    @Override
    public void getStoreData2(Map<String, Object> request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.HiddenDangerStoreList2.getUrl()));
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

        new XUtils2.BuilderGetHttpData()
        xUtils.setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<HiddenDanger> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getPage().getList()),
                                    new TypeToken<ArrayList<HiddenDanger>>() {
                                    }.getType()
                            );

                            if (tempData != null) {
                                for (HiddenDanger t : tempData) {
                                    if (TextUtils.isEmpty(t.getCheckDate())) {
                                        t.setCheckDateShow("0");
                                    } else {
                                        t.setCheckDateShow(StringUtils.getDateLong(t.getCheckDate(), StringUtils.DateFormatMode.LINE_YMD) + "");
                                    }
                                    if (t.getLevel() != null) {
                                        HiddenLevel hiddenLevel = null;

                                        for (HiddenLevel p : Constants.HIDDEN_LEVEL) {
                                            if (p.getLevelId() == t.getLevel()) {
                                                hiddenLevel = p;
                                            }
                                        }

                                        if (hiddenLevel == null) {
                                            t.setHiddenLevel("");
                                        } else {
                                            t.setHiddenLevel(hiddenLevel.getLevelName());
                                        }
                                    }
                                    if (t.getHiddenPhotoList() != null) {
                                        t.getMedia().put(25, UploadFileUtils.webFiles2AppFiles(t.getHiddenPhotoList()));
                                    }
                                    if (t.getChangePhotoList() != null) {
                                        t.getMedia().put(1, UploadFileUtils.webFiles2AppFiles(t.getChangePhotoList()));
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
        });
        xUtils.getHttpData(HttpMethod.GET, params);
    }

    @Override
    public void deleteData(ArrayList<Long> request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.HiddenDangerDelete.getUrl()));
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
        });
        xUtils.getHttpData(HttpMethod.POST, params);
    }

    @Override
    public void submitService(Map<String, Object> request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.ServiceSubmit.getUrl()));
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
                        long newServiceId = new BigDecimal(responseInfo.getData() + "").longValue();
                        mView.submitServiceSuccess(newServiceId);
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

                        mView.submitServiceError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
                    mView.submitServiceError();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {
                    mView.submitServiceFinish();
                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        });
        xUtils.getHttpData(HttpMethod.POST, params);
    }
}
