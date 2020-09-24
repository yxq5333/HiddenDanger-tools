package com.xhtt.hiddendanger.UI.Activity.Main;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;

/**
 * 主界面管理层
 *
 * @author HG
 */

public class HiddenDangerPresenter extends BasePresenter<HiddenDangerContract.View, HiddenDangerContract.Model> implements HiddenDangerContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new HiddenDangerModel(mView);
    }

}
