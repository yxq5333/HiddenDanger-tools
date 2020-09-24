package com.xhtt.hiddendanger.UI.Fragment.Statistics;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 隐患统计管理层
 *
 * @author HG
 */

public class HiddenStatisticsPresenter extends BasePresenter<HiddenStatisticsContract.View, HiddenStatisticsContract.Model> implements HiddenStatisticsContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new HiddenStatisticsModel(mView);
    }

    @Override
    public void getBaseData(int year) {

        Map<String, Object> request = new HashMap<>();
        request.put("currentYear", year);

        mModel.getBaseData(request);
    }

    @Override
    public void getListData(int pageIndex, int pageSize, int year) {

        Map<String, Object> request = new HashMap<>();
        request.put("currentYear", year);
        request.put("page", pageIndex);
        request.put("limit", pageSize);

        mModel.getListData(request);
    }

}
