package com.xhtt.hiddendangermaster.ui.activity.login;

import com.google.gson.Gson;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.hg.hollowgoods.util.xutils.XUtils2;
import com.hg.hollowgoods.util.xutils.callback.base.GetHttpDataListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.LoginRequest;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

/**
 * 主界面数据层
 *
 * @author HG
 */

public class LoginModel implements LoginContract.Model {

    private LoginContract.View mView;

    public LoginModel(LoginContract.View mView) {
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
    public void doLogin(LoginRequest request) {

        if (isViewAttached()) {
            mView.doLoginStart();
        }

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.Login.getUrl()));
        params.setMethod(HttpMethod.POST);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(request));

        new XUtils2.BuilderGetHttpData().setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {
                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        checkUserRole(responseInfo.getToken());
                    } else {
                        t.error(responseInfo.getMsg());
                        mView.doLoginError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
                    mView.doLoginError();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {
                    mView.doLoginFinish();
                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        }).getHttpData(params);
    }

    private void checkUserRole(String token) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.GetUserData.getUrl()));
        params.setMethod(HttpMethod.GET);
        params.addHeader("token", token);

        new XUtils2.BuilderGetHttpData().setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        User user = new Gson().fromJson(
                                new Gson().toJson(responseInfo.getData()),
                                User.class
                        );

                        if (user.getRoleIdList() != null && user.getRoleIdList().size() > 0) {
                            boolean flag = true;

                            for (int p : user.getRoleIdList()) {
                                if (p == User.USER_TYPE_GOV) {
                                    t.error("政府端未开放");
                                    flag = false;
                                    mView.doLoginError();
                                    break;
                                }
                            }

                            if (flag) {
                                mView.doLoginSuccess(token);
                            }
                        } else {
                            mView.doLoginSuccess(token);
                        }
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

                        mView.doLoginError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
                    mView.doLoginError();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {

            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        }).getHttpData(params);
    }

}
