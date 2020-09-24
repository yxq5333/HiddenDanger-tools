package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.IBaseModel;
import com.hg.hollowgoods.UI.Base.MVP.IBaseView;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;

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
