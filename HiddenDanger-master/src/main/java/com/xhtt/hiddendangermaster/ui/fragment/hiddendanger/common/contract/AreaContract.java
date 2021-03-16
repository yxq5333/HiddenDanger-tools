package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.common.contract;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.CommonChooseItem;

import java.util.ArrayList;
import java.util.Map;

/**
 * 省市区镇协议层
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaContract {

    public interface Model extends ZIBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends ZIBaseView {

        void getDataSuccess(ArrayList<CommonChooseItem> tempData);

        void getDataError(Object msg);

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String searchKey, long parentId);
    }

}
