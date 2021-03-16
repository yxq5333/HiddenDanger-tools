package com.xhtt.hiddendangermaster.ui.fragment.companymap.presenter;

import android.content.Context;

import com.hg.zero.ui.base.mvp.ZBasePresenter;
import com.xhtt.hiddendangermaster.ui.fragment.companymap.contract.CompanyMapContract;
import com.xhtt.hiddendangermaster.ui.fragment.companymap.model.CompanyMapModel;

/**
 * 企业地图管理层
 * <p>
 * Created by Hollow Goods on 2020-04-01
 */

public class CompanyMapPresenter extends ZBasePresenter<CompanyMapContract.View, CompanyMapContract.Model> implements CompanyMapContract.Presenter {

    public CompanyMapPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void afterAttachView() {
        mModel = new CompanyMapModel(mView, mContext);
    }

    @Override
    public void getData() {
        mModel.getData();
    }

    @Override
    public void addServiceCount(Object companyId) {

//        Map<String, Object> request = new HashMap<>();
//        request.put("companyId", companyId);

        mModel.addServiceCount(companyId);
    }

    @Override
    public void export() {
        mModel.export();
    }

}
