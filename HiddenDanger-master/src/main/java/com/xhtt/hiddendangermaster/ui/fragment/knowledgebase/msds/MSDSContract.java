package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.msds;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDS;

import java.util.ArrayList;
import java.util.Map;

/**
 * MSDS协议层
 *
 * @author HG
 */

public class MSDSContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<MSDS> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key, int searchType);
    }

}
