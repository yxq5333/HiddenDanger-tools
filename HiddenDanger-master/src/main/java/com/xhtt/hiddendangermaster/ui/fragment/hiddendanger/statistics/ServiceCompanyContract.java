package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.statistics;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.statistics.ServiceCompany;
import com.xhtt.hiddendangermaster.bean.hiddendanger.statistics.ServiceCompanyBase;

import java.util.ArrayList;
import java.util.Map;

/**
 * 服务企业协议层
 *
 * @author HG
 */

public class ServiceCompanyContract {

    public interface Model extends IBaseModel {
        void getBaseData(Map<String, Object> request);

        void getListData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getBaseDataSuccess(ServiceCompanyBase serviceCompanyBase);

        void getBaseDataError();

        void getBaseDataFinish();

        void getListDataSuccess(ArrayList<ServiceCompany> tempData);

        void getListDataError();

        void getListDataFinish();
    }

    public interface Presenter {
        void getBaseData(int year);

        void getListData(int pageIndex, int pageSize, int year);
    }

}
