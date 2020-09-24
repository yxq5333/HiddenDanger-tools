package com.xhtt.hiddendangermaster.ui.activity.login;

import android.text.TextUtils;

import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BasePresenter;
import com.xhtt.hiddendangermaster.bean.LoginRequest;

/**
 * 主界面管理层
 *
 * @author HG
 */

public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> implements LoginContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new LoginModel(mView);
    }

    @Override
    public void doLogin(String username, String password) {

        if (TextUtils.isEmpty(username)) {
            t.warning("请输入用户名/手机号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            t.warning("请输入密码");
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        mModel.doLogin(request);
    }
}
