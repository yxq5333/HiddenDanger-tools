package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.statistics;

/**
 * 统计报表数据层
 *
 * @author HG
 */

public class StatisticsModel implements StatisticsContract.Model {

    private StatisticsContract.View mView;

    public StatisticsModel(StatisticsContract.View mView) {
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
