package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;

import java.util.ArrayList;
import java.util.Map;

/**
 * 选择企业协议层
 *
 * @author HG
 */

public class CompanySelectorContract {

    public interface Model extends ZIBaseModel {
        void getData(Map<String, Object> request);

        void findName(Map<String, Object> request);
    }

    public interface View extends ZIBaseView {
        void getDataSuccess(ArrayList<Company> tempData);

        void getDataError();

        void getDataFinish();

        void findNameSuccess(ArrayList<Company> tempData);

        void findNameError();

        void findNameFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key);

        void findName(String key);
    }

}
