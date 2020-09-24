package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.technologystandard;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.technologystandard.TechnologyStandard;

import java.util.ArrayList;
import java.util.Map;

/**
 * 技术标准协议层
 *
 * @author HG
 */

public class TechnologyStandardContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<TechnologyStandard> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key);
    }

}
