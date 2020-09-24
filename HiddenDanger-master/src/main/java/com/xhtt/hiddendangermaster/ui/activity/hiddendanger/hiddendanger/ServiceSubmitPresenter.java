package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.content.Context;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.ServiceSubmit;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务提交管理层
 * <p>
 * Created by Hollow Goods on 2019-05-22
 */

public class ServiceSubmitPresenter extends BasePresenter<ServiceSubmitContract.View, ServiceSubmitContract.Model> implements ServiceSubmitContract.Presenter {

    public ServiceSubmitPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void afterAttachView() {
        mModel = new ServiceSubmitModel(mView, mContext);
    }

    @Override
    public void getData(long serviceId) {
        mModel.getData(serviceId);
    }

    @Override
    public void submitService(long serviceId, int dayCount, ServiceSubmit serviceSubmit) {

        Map<String, Object> request = new HashMap<>();
        request.put("id", serviceId);
        request.put("dayCount", dayCount);
        request.put("checkUserNamePhotoList", serviceSubmit.getCheckUserNamePhotoList());
        request.put("chargePersonPhotoList", serviceSubmit.getChargePersonPhotoList());

        mModel.submitService(request);
    }
}
