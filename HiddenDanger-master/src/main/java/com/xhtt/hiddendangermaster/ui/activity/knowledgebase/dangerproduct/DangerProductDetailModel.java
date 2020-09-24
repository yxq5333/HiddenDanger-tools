package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct;

/**
 * 危化品信息数据层
 *
 * @author HG
 */

public class DangerProductDetailModel implements DangerProductDetailContract.Model {

    private DangerProductDetailContract.View mView;

    public DangerProductDetailModel(DangerProductDetailContract.View mView) {
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
