package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.accidentcase;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 主界面管理层
 *
 * @author HG
 */

public class AccidentCasePresenter extends BasePresenter<AccidentCaseContract.View, AccidentCaseContract.Model> implements AccidentCaseContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new AccidentCaseModel(mView);
    }

    @Override
    public void getData(int pageIndex, int pageSize, String key) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("title", key);
        request.put("showContent", 1);

        mModel.getData(request);
    }

    @Override
    public void getHotData(int pageIndex, int pageSize, String key) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("title", key);
        request.put("toHot", true);
        request.put("showContent", 1);

        mModel.getHotData(request);
    }

    @Override
    public void addReadCount(long id) {
        mModel.addReadCount(id);
    }
}
