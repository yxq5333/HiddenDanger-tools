package com.xhtt.hiddendangermaster.ui.fragment.companymap.contract;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.CompanyMap;

import java.io.File;
import java.util.ArrayList;

/**
 * 企业地图协议层
 * <p>
 * Created by Hollow Goods on 2020-04-01
 */

public class CompanyMapContract {

    public interface Model extends IBaseModel {
        void getData();

        void addServiceCount(/*Map<String, Object> request*/Object companyId);

        void export();
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<CompanyMap> tempData);

        void getDataError();

        void getDataFinish();

        void addServiceCountSuccess();

        void addServiceCountError();

        void addServiceCountFinish();

        void exportSuccess(File file);

        void exportError();

        void exportFinish();
    }

    public interface Presenter {
        void getData();

        void addServiceCount(Object companyId);

        void export();
    }

}
