package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

/**
 * 危化品信息管理层
 *
 * @author HG
 */

public class DangerProductDetailPresenter extends BasePresenter<DangerProductDetailContract.View, DangerProductDetailContract.Model> implements DangerProductDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new DangerProductDetailModel(mView);
    }

}
