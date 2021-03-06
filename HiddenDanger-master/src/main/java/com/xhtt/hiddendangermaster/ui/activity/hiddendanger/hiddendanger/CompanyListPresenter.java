package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业信息管理层
 *
 * @author HG
 */

public class CompanyListPresenter extends ZBasePresenter<CompanyListContract.View, CompanyListContract.Model> implements CompanyListContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new CompanyListModel(mView);
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
    public void deleteData(long id) {

        ArrayList<Long> request = new ArrayList<>();
        request.add(id);

        mModel.deleteData(request);
    }

    @Override
    public void getHiddenLevel() {
        mModel.getHiddenLevel();
    }
}
