package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import com.google.gson.Gson;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.XUtils.GetHttpDataListener;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDangerDetailRequest;
import com.xhtt.hiddendanger.Bean.ResponseInfo;
import com.xhtt.hiddendanger.Constant.InterfaceApi;
import com.xhtt.hiddendanger.R;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.math.BigDecimal;

/**
 * 隐患排查数据层
 *
 * @author HG
 */

public class HiddenDangerDetailModel implements HiddenDangerDetailContract.Model {

    private HiddenDangerDetailContract.View mView;

    public HiddenDangerDetailModel(HiddenDangerDetailContract.View mView) {
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
    public void addData(HiddenDangerDetailRequest request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.HiddenDangerAdd.getUrl()));
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
                        long id = new BigDecimal(responseInfo.getData() + "").longValue();
                        mView.submitDataSuccess(id);
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
    public void editData(HiddenDangerDetailRequest request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.HiddenDangerEdit.getUrl()));
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
                        mView.submitDataSuccess(null);
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
    public void submitData(HiddenDangerDetailRequest request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.HiddenDangerSubmit.getUrl()));
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
                        mView.submitDataSuccess(null);
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
    public void getUserData() {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.GetUserData.getUrl()));
        params.addHeader("token", MyApplication.createApplication().getToken());

        new XUtils2.BuilderGetHttpData()
        xUtils.setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        User user = new Gson().fromJson(
                                new Gson().toJson(responseInfo.getData()),
                                User.class
                        );

                        mView.getUserDataSuccess(user);
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
        });
        xUtils.getHttpData(HttpMethod.GET, params);
    }

}
