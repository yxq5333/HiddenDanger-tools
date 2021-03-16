package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.statistics;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

/**
 * 统计报表管理层
 *
 * @author HG
 */

public class StatisticsPresenter extends ZBasePresenter<StatisticsContract.View, StatisticsContract.Model> implements StatisticsContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new StatisticsModel(mView);
    }

}
