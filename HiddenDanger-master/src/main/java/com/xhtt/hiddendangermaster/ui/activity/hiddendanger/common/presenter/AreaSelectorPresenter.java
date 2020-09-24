package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.presenter;

import android.content.Context;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.contract.AreaSelectorContract;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.model.AreaSelectorModel;

/**
 * 地址选择管理层
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaSelectorPresenter extends BasePresenter<AreaSelectorContract.View, AreaSelectorContract.Model> implements AreaSelectorContract.Presenter {

    public AreaSelectorPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void afterAttachView() {
        mModel = new AreaSelectorModel(mView, mContext);
    }

}
