package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.common.presenter;

import android.content.Context;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.common.contract.AreaContract;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.common.model.AreaModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 省市区镇管理层
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaPresenter extends BasePresenter<AreaContract.View, AreaContract.Model> implements AreaContract.Presenter {

    public AreaPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void afterAttachView() {
        mModel = new AreaModel(mView, mContext);
    }

    @Override
    public void getData(int pageIndex, int pageSize, String searchKey, long parentId) {

        Map<String, Object> request = new HashMap<>();
        request.put("pageIndex", pageIndex);
        request.put("pageSize", pageSize);
        request.put("searchKey", searchKey);
        request.put("parentId", parentId);

        mModel.getData(request);
    }

}
