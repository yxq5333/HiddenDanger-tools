package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

/**
 * 随拍随记数据层
 *
 * @author HG
 */

public class FreeTakeModel implements FreeTakeContract.Model {

    private FreeTakeContract.View mView;

    public FreeTakeModel(FreeTakeContract.View mView) {
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
