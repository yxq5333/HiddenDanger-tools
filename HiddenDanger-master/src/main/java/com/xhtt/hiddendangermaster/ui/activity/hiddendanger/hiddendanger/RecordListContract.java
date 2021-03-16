package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Record;

import java.util.ArrayList;
import java.util.Map;

/**
 * 服务记录协议层
 *
 * @author HG
 */

public class RecordListContract {

    public interface Model extends ZIBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends ZIBaseView {
        void getDataSuccess(ArrayList<Record> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(long companyId, int pageIndex, int pageSize);
    }

}
