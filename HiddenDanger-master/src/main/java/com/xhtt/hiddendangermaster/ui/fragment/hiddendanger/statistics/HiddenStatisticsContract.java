package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.statistics;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.statistics.HiddenStatistics;
import com.xhtt.hiddendangermaster.bean.hiddendanger.statistics.HiddenStatisticsBase;

import java.util.ArrayList;
import java.util.Map;

/**
 * 隐患统计协议层
 *
 * @author HG
 */

public class HiddenStatisticsContract {

    public interface Model extends ZIBaseModel {
        void getBaseData(Map<String, Object> request);

        void getListData(Map<String, Object> request);
    }

    public interface View extends ZIBaseView {
        void getBaseDataSuccess(HiddenStatisticsBase hiddenStatisticsBase);

        void getBaseDataError();

        void getBaseDataFinish();

        void getListDataSuccess(ArrayList<HiddenStatistics> tempData);

        void getListDataError();

        void getListDataFinish();
    }

    public interface Presenter {
        void getBaseData(int year);

        void getListData(int pageIndex, int pageSize, int year);
    }

}
