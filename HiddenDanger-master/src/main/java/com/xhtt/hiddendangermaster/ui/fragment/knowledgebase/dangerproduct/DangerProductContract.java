package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.dangerproduct;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.dangerproduct.DangerProduct;

import java.util.ArrayList;
import java.util.Map;

/**
 * 危化品安全信息协议层
 *
 * @author HG
 */

public class DangerProductContract {

    public interface Model extends ZIBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends ZIBaseView {
        void getDataSuccess(ArrayList<DangerProduct> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key, int searchType);
    }

}
