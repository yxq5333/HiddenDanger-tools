package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;

/**
 * 随拍随记管理层
 *
 * @author HG
 */

public class FreeTakePresenter extends BasePresenter<FreeTakeContract.View, FreeTakeContract.Model> implements FreeTakeContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new FreeTakeModel(mView);
    }

}
