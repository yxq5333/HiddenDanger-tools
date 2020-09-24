package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.laws;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;
import com.xhtt.hiddendangermaster.constant.LawType;

import java.util.HashMap;
import java.util.Map;

/**
 * 法律法规管理层
 *
 * @author HG
 */

public class LawsPresenter extends BasePresenter<LawsContract.View, LawsContract.Model> implements LawsContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new LawsModel(mView);
    }

    @Override
    public void getData(int pageIndex, int pageSize, String key, LawType type) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("title", key);
        request.put("type", type.getValue());

        mModel.getData(request);
    }
}
