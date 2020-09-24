package com.xhtt.hiddendangermaster.ui.activity.register;

import android.text.TextUtils;

import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BasePresenter;
import com.hg.hollowgoods.util.PhoneUtils;
import com.hg.hollowgoods.util.RegexUtils;
import com.xhtt.hiddendangermaster.bean.RegisterRequest;

/**
 * 注册管理层
 *
 * @author HG
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View, RegisterContract.Model> implements RegisterContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new RegisterModel(mView);
    }

    @Override
    public void doRegister(String phone, String name, String username, String inputCode, String realCode, String password) {

        if (!PhoneUtils.isPhoneNumber(phone)) {
            t.warning("手机号不合法");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            t.warning("姓名必填");
            return;
        }

        if (!TextUtils.isEmpty(username)) {
            if (RegexUtils.isRealNumber1(username)) {
                t.warning("登录账号不能为纯数字");
                return;
            }
        } else {
            username = phone;
        }

        if (!TextUtils.equals(inputCode, realCode.toLowerCase())) {
            t.warning("验证码输入不正确");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            t.warning("密码必填");
            return;
        }

        if (password.length() < 6) {
            t.warning("密码至少6位字符");
            return;
        }

        if (password.length() > 20) {
            t.warning("密码最多20位字符");
            return;
        }

        RegisterRequest request = new RegisterRequest(phone, name, username, password);
        mModel.doRegister(request);
    }
}
