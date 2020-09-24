package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.dangerproduct;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 危化品安全信息管理层
 *
 * @author HG
 */

public class DangerProductPresenter extends BasePresenter<DangerProductContract.View, DangerProductContract.Model> implements DangerProductContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new DangerProductModel(mView);
    }

    @Override
    public void getData(int pageIndex, int pageSize, String key, int searchType) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("nameProd", key);
        request.put("type", searchType);

        mModel.getData(request);
    }
}
