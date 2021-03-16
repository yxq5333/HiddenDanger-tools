package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

/**
 * 危化品信息管理层
 *
 * @author HG
 */

public class DangerProductDetailPresenter extends ZBasePresenter<DangerProductDetailContract.View, DangerProductDetailContract.Model> implements DangerProductDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new DangerProductDetailModel(mView);
    }

}
