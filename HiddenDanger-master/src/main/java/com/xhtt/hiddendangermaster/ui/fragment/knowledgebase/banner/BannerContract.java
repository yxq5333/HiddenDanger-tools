package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.banner;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.banner.Banner;

import java.util.ArrayList;
import java.util.Map;

/**
 * 轮播图协议层
 *
 * @author HG
 */

public class BannerContract {

    public interface Model extends ZIBaseModel {
        void getData(Map<String, Object> request);
    }

    public interface View extends ZIBaseView {
        void getDataSuccess(ArrayList<Banner> tempData);

        void getDataError();

        void getDataFinish();
    }

    public interface Presenter {
        void getData(int location);
    }

}
