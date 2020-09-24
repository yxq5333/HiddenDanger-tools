package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.IBaseModel;
import com.hg.hollowgoods.UI.Base.MVP.IBaseView;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Record;

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
