package com.xhtt.hiddendanger.UI.Activity.Main;

/**
 * 主界面数据层
 *
 * @author HG
 */

public class HiddenDangerModel implements HiddenDangerContract.Model {

    private HiddenDangerContract.View mView;

    public HiddenDangerModel(HiddenDangerContract.View mView) {
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
