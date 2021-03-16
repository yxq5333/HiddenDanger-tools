package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

/**
 * 随拍随记管理层
 *
 * @author HG
 */

public class FreeTakePresenter extends ZBasePresenter<FreeTakeContract.View, FreeTakeContract.Model> implements FreeTakeContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new FreeTakeModel(mView);
    }

}
