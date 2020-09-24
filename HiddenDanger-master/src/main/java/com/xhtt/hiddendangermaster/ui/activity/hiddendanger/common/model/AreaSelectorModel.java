package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.model;

import android.content.Context;

import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.contract.AreaSelectorContract;

/**
 * 地址选择数据层
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaSelectorModel implements AreaSelectorContract.Model {

    private AreaSelectorContract.View mView;
    private Context mContext;

    public AreaSelectorModel(AreaSelectorContract.View mView, Context mContext) {
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
