package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds;

/**
 * MSDS数据层
 *
 * @author HG
 */

public class MSDSDetailModel implements MSDSDetailContract.Model {

    private MSDSDetailContract.View mView;

    public MSDSDetailModel(MSDSDetailContract.View mView) {
        this.mView = mView;
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
