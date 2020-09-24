package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.content.Context;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

/**
 * 服务单签字确认 管理层
 * <p>
 * Created by YXQ on 2020-06-23
 */

public class SignPresenter extends BasePresenter<SignContract.View, SignContract.Model> implements SignContract.Presenter {

    public SignPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void afterAttachView() {
        mModel = new SignModel(mView, mContext);
    }

}
