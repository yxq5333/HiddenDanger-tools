package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;

import java.util.ArrayList;
import java.util.Map;

/**
 * 企业信息协议层
 *
 * @author HG
 */

public class CompanyListContract {

    public interface Model extends ZIBaseModel {
        void getData(Map<String, Object> request);

        void deleteData(ArrayList<Long> request);

        void getHiddenLevel();
    }

    public interface View extends ZIBaseView {
        void getDataSuccess(ArrayList<Company> tempData);

        void getDataError();

        void getDataFinish();

        void deleteDataSuccess();

        void deleteDataError();

        void deleteDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key);

        void deleteData(long id);

        void getHiddenLevel();
    }

}
