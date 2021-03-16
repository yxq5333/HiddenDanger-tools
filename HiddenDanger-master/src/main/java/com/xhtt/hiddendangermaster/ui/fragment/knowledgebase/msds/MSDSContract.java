package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.msds;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDS;

import java.util.ArrayList;
import java.util.Map;

/**
 * MSDS协议层
 *
 * @author HG
 */

public class MSDSContract {

    public interface Model extends ZIBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends ZIBaseView {
        void getDataSuccess(ArrayList<MSDS> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key, int searchType);
    }

}
