package com.xhtt.hiddendangermaster.ui.fragment.profile;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的管理层
 *
 * @author HG
 */

public class ProfilePresenter extends ZBasePresenter<ProfileContract.View, ProfileContract.Model> implements ProfileContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new ProfileModel(mView);
    }

    @Override
    public void getData() {
        mModel.getData();
    }

    @Override
    public void updateUserData(String username, String name) {

        Map<String, Object> request = new HashMap<>();
        request.put("username", username);
        request.put("name", name);

        mModel.updateUserData(request);
    }
}
