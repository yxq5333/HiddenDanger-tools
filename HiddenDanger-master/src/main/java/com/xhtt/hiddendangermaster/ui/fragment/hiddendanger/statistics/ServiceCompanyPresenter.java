package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.statistics;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务企业管理层
 *
 * @author HG
 */

public class ServiceCompanyPresenter extends BasePresenter<ServiceCompanyContract.View, ServiceCompanyContract.Model> implements ServiceCompanyContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new ServiceCompanyModel(mView);
    }

    @Override
    public void getBaseData(int year) {

        Map<String, Object> request = new HashMap<>();
        request.put("currentYear", year);

        mModel.getBaseData(request);
    }

    @Override
    public void getListData(int pageIndex, int pageSize, int year) {

        Map<String, Object> request = new HashMap<>();
        request.put("currentYear", year);
        request.put("page", pageIndex);
        request.put("limit", pageSize);

        mModel.getListData(request);
    }
}
