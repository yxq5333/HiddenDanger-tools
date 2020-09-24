package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 选择企业管理层
 *
 * @author HG
 */

public class CompanySelectorPresenter extends BasePresenter<CompanySelectorContract.View, CompanySelectorContract.Model> implements CompanySelectorContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new CompanySelectorModel(mView);
    }

    @Override
    public void getData(int pageIndex, int pageSize, String key) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("name", key);

        mModel.getData(request);
    }

    @Override
    public void findName(String key) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", 1);
        request.put("limit", 10);
        request.put("name", key);

        mModel.findName(request);
    }
}
