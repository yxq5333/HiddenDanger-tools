package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

/**
 * MSDS管理层
 *
 * @author HG
 */

public class MSDSDetailPresenter extends BasePresenter<MSDSDetailContract.View, MSDSDetailContract.Model> implements MSDSDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new MSDSDetailModel(mView);
    }

}
