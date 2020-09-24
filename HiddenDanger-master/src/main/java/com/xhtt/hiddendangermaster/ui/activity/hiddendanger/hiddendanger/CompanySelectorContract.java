package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;

import java.util.ArrayList;
import java.util.Map;

/**
 * 选择企业协议层
 *
 * @author HG
 */

public class CompanySelectorContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);

        void findName(Map<String, Object> request);
    }

    public interface View extends IBaseView {
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
