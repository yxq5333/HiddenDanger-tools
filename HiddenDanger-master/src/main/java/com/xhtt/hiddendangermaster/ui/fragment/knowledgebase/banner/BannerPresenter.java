package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.banner;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 轮播图管理层
 *
 * @author HG
 */

public class BannerPresenter extends BasePresenter<BannerContract.View, BannerContract.Model> implements BannerContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new BannerModel(mView);
    }

    @Override
    public void getData(int location) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", 1);
        request.put("limit", 10);
        request.put("showPic", 1);
        request.put("location", location);

        mModel.getData(request);
    }
}
