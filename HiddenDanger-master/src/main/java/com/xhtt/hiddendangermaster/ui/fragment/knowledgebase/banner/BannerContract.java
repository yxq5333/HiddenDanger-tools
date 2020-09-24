package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.banner;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.banner.Banner;

import java.util.ArrayList;
import java.util.Map;

/**
 * 轮播图协议层
 *
 * @author HG
 */

public class BannerContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<Banner> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int location);
    }

}
