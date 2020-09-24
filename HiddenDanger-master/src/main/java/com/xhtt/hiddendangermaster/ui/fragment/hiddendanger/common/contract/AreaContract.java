package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.common.contract;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.CommonChooseItem;

import java.util.ArrayList;
import java.util.Map;

/**
 * 省市区镇协议层
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends IBaseView {

        void getDataSuccess(ArrayList<CommonChooseItem> tempData);

        void getDataError(Object msg);

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String searchKey, long parentId);
    }

}
