package com.xhtt.hiddendanger.UI.Activity.Common.Presenter;

import android.content.Context;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;
import com.xhtt.hiddendanger.UI.Activity.Common.Contract.AreaSelectorContract;
import com.xhtt.hiddendanger.UI.Activity.Common.Model.AreaSelectorModel;

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
