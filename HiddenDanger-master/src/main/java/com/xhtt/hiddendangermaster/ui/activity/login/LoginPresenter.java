package com.xhtt.hiddendangermaster.ui.activity.login;

import android.text.TextUtils;

import com.hg.zero.toast.Zt;
import com.hg.zero.ui.base.mvp.ZBasePresenter;
import com.xhtt.hiddendangermaster.bean.LoginRequest;

/**
 * 主界面管理层
 *
 * @author HG
 */

public class LoginPresenter extends ZBasePresenter<LoginContract.View, LoginContract.Model> implements LoginContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new LoginModel(mView);
    }

    @Override
    public void doLogin(String username, String password) {

        if (TextUtils.isEmpty(username)) {
            Zt.warning("请输入用户名/手机号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Zt.warning("请输入密码");
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        mModel.doLogin(request);
    }
}
