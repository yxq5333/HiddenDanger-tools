package com.xhtt.hiddendangermaster.ui.activity.profile;

import android.text.TextUtils;

import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码管理层
 *
 * @author HG
 */

public class AlterPasswordPresenter extends BasePresenter<AlterPasswordContract.View, AlterPasswordContract.Model> implements AlterPasswordContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new AlterPasswordModel(mView);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String reNewPassword) {

        if (TextUtils.isEmpty(oldPassword)) {
            t.warning("请输入原密码");
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            t.warning("请输入新密码");
            return;
        }

        if (TextUtils.isEmpty(reNewPassword)) {
            t.warning("请输入再次输入新密码");
            return;
        }

        if (newPassword.length() < 6) {
            t.warning("密码至少6位字符");
            return;
        }

        if (newPassword.length() > 20) {
            t.warning("密码最多20位字符");
            return;
        }

        if (!TextUtils.equals(newPassword, reNewPassword)) {
            t.warning("两次密码不一致");
            return;
        }

        Map<String, Object> request = new HashMap<>();
        request.put("password", oldPassword);
        request.put("newPassword", newPassword);

        mModel.changePassword(request);
    }
}
