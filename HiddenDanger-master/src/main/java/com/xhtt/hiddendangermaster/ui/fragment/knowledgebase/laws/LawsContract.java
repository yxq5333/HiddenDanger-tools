package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.laws;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.laws.Laws;
import com.xhtt.hiddendangermaster.constant.LawType;

import java.util.ArrayList;
import java.util.Map;

/**
 * 法律法规协议层
 *
 * @author HG
 */

public class LawsContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<Laws> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key, LawType type);
    }

}
