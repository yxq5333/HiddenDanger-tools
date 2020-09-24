package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

/**
 * 隐患排查单次服务列表数据层
 *
 * @author HG
 */

public class HiddenDangerOnceListModel implements HiddenDangerOnceListContract.Model {

    private HiddenDangerOnceListContract.View mView;

    public HiddenDangerOnceListModel(HiddenDangerOnceListContract.View mView) {
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
