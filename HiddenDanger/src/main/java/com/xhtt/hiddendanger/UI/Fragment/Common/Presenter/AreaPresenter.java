package com.xhtt.hiddendanger.UI.Fragment.Common.Presenter;

import android.content.Context;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;
import com.xhtt.hiddendanger.UI.Fragment.Common.Contract.AreaContract;
import com.xhtt.hiddendanger.UI.Fragment.Common.Model.AreaModel;

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
