package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

/**
 * 随拍随记管理层
 *
 * @author HG
 */

public class FreeTakePresenter extends BasePresenter<FreeTakeContract.View, FreeTakeContract.Model> implements FreeTakeContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new FreeTakeModel(mView);
    }

}
