package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Record;

import java.util.ArrayList;
import java.util.Map;

/**
 * 服务记录协议层
 *
 * @author HG
 */

public class RecordListContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<Record> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(long companyId, int pageIndex, int pageSize);
    }

}
