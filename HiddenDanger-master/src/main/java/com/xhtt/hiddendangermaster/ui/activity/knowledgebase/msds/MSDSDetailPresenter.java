package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

/**
 * MSDS管理层
 *
 * @author HG
 */

public class MSDSDetailPresenter extends ZBasePresenter<MSDSDetailContract.View, MSDSDetailContract.Model> implements MSDSDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new MSDSDetailModel(mView);
    }

}
