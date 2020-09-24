package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;

import java.util.ArrayList;
import java.util.Map;

/**
 * 企业信息协议层
 *
 * @author HG
 */

public class CompanyListContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);

        void deleteData(ArrayList<Long> request);

        void getHiddenLevel();
    }

    public interface View extends IBaseView {
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
