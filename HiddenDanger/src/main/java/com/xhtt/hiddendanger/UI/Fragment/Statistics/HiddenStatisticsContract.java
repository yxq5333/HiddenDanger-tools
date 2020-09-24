package com.xhtt.hiddendanger.UI.Fragment.Statistics;

import com.hg.hollowgoods.UI.Base.MVP.IBaseModel;
import com.hg.hollowgoods.UI.Base.MVP.IBaseView;
import com.xhtt.hiddendanger.Bean.Statistics.HiddenStatistics;
import com.xhtt.hiddendanger.Bean.Statistics.HiddenStatisticsBase;

import java.util.ArrayList;
import java.util.Map;

/**
 * 隐患统计协议层
 *
 * @author HG
 */

public class HiddenStatisticsContract {

    public interface Model extends IBaseModel {
        void getBaseData(Map<String, Object> request);

        void getListData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
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
