package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.statistics;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

/**
 * 统计报表管理层
 *
 * @author HG
 */

public class StatisticsPresenter extends BasePresenter<StatisticsContract.View, StatisticsContract.Model> implements StatisticsContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new StatisticsModel(mView);
    }

}
