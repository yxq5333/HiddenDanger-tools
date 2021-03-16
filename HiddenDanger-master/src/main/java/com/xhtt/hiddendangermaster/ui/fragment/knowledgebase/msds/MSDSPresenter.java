package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.msds;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * MSDS管理层
 *
 * @author HG
 */

public class MSDSPresenter extends ZBasePresenter<MSDSContract.View, MSDSContract.Model> implements MSDSContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new MSDSModel(mView);
    }

    @Override
    public void getData(int pageIndex, int pageSize, String key, int searchType) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("chemicalsNameCn", key);
        request.put("type", searchType);

        mModel.getData(request);
    }

}
