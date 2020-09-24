package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.dangerproduct;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.dangerproduct.DangerProduct;

import java.util.ArrayList;
import java.util.Map;

/**
 * 危化品安全信息协议层
 *
 * @author HG
 */

public class DangerProductContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<DangerProduct> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key, int searchType);
    }

}
