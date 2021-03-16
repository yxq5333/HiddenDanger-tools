package com.xhtt.hiddendangermaster.ui.activity.register;

import android.text.TextUtils;

import com.hg.zero.toast.Zt;
import com.hg.zero.ui.base.mvp.ZBasePresenter;
import com.hg.zero.util.ZPhoneUtils;
import com.hg.zero.util.ZRegexUtils;
import com.xhtt.hiddendangermaster.bean.RegisterRequest;

/**
 * 注册管理层
 *
 * @author HG
 */
public class RegisterPresenter extends ZBasePresenter<RegisterContract.View, RegisterContract.Model> implements RegisterContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new RegisterModel(mView);
    }

    @Override
    public void doRegister(String phone, String name, String username, String inputCode, String realCode, String password) {

        if (!ZPhoneUtils.isPhoneNumber(phone)) {
            Zt.warning("手机号不合法");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Zt.warning("姓名必填");
            return;
        }

        if (!TextUtils.isEmpty(username)) {
            if (ZRegexUtils.isRealNumber1(username)) {
                Zt.warning("登录账号不能为纯数字");
                return;
            }
        } else {
            username = phone;
        }

        if (!TextUtils.equals(inputCode, realCode.toLowerCase())) {
            Zt.warning("验证码输入不正确");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Zt.warning("密码必填");
            return;
        }

        if (password.length() < 6) {
            Zt.warning("密码至少6位字符");
            return;
        }

        if (password.length() > 20) {
            Zt.warning("密码最多20位字符");
            return;
        }

        RegisterRequest request = new RegisterRequest(phone, name, username, password);
        mModel.doRegister(request);
    }
}
