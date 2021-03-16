package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

/**
 * 隐患排查单次服务列表管理层
 *
 * @author HG
 */

public class HiddenDangerOnceListPresenter extends ZBasePresenter<HiddenDangerOnceListContract.View, HiddenDangerOnceListContract.Model> implements HiddenDangerOnceListContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new HiddenDangerOnceListModel(mView);
    }

}
