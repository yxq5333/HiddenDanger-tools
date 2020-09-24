package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;

/**
 * 隐患排查单次服务列表管理层
 *
 * @author HG
 */

public class HiddenDangerOnceListPresenter extends BasePresenter<HiddenDangerOnceListContract.View, HiddenDangerOnceListContract.Model> implements HiddenDangerOnceListContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new HiddenDangerOnceListModel(mView);
    }

}
