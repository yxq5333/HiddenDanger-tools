package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.technologystandard;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 技术标准管理层
 *
 * @author HG
 */

public class TechnologyStandardPresenter extends BasePresenter<TechnologyStandardContract.View, TechnologyStandardContract.Model> implements TechnologyStandardContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new TechnologyStandardModel(mView);
    }

    @Override
    public void getData(int pageIndex, int pageSize, String key) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("title", key);

        mModel.getData(request);
    }
}
