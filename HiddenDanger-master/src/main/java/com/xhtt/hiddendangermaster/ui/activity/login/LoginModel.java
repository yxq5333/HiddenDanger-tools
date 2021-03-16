package com.xhtt.hiddendangermaster.ui.activity.login;

import com.google.gson.Gson;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.net.ZxUtils3;
import com.hg.zero.net.callback.base.ZRequestDataListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
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

        RequestParams params = new RequestParams(ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.Login.getUrl()));
        params.setMethod(HttpMethod.POST);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(request));

        new ZxUtils3.RequestDataBuilder().setRequestDataListener(new ZRequestDataListener() {
            @Override
            public void onGetSuccess(String result) {
                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        checkUserRole(responseInfo.getToken());
                    } else {
                        Zt.error(responseInfo.getMsg());
                        mView.doLoginError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    Zt.error(R.string.network_error);
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
        }).requestData(params);
    }

    private void checkUserRole(String token) {

        RequestParams params = new RequestParams(ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.GetUserData.getUrl()));
        params.setMethod(HttpMethod.GET);
        params.addHeader("token", token);

        new ZxUtils3.RequestDataBuilder().setRequestDataListener(new ZRequestDataListener() {
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
                                    Zt.error("政府端未开放");
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
                            Zt.error(responseInfo.getMsg());
                        } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                            Zt.error("授权已过期，请重新登录");
                            ZEvent event = new ZEvent(EventActionCode.TokenOverdue);
                            EventBus.getDefault().post(event);
                        } else {
                            Zt.error(R.string.network_error);
                        }

                        mView.doLoginError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    Zt.error(R.string.network_error);
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
        }).requestData(params);
    }

}
