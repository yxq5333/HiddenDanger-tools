package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.content.Context;

/**
 * 服务单签字确认 数据层
 * <p>
 * Created by YXQ on 2020-06-23
 */

public class SignModel implements SignContract.Model {

    private SignContract.View mView;
    private Context mContext;

    public SignModel(SignContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

}
