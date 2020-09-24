package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查表管理层
 *
 * @author HG
 */

public class CheckTableListPresenter extends BasePresenter<CheckTableListContract.View, CheckTableListContract.Model> implements CheckTableListContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new CheckTableListModel(mView);
    }

    @Override
    public void getData(long companyId, int pageIndex, int pageSize, long serviceId, String key) {

        Map<String, Object> request = new HashMap<>();
        request.put("companyId", companyId);
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("name", key);
        request.put("serviceId", serviceId);

        mModel.getData(request);
    }

    @Override
    public void deleteData(long id) {

        ArrayList<Long> request = new ArrayList<>();
        request.add(id);

        mModel.deleteData(request);
    }
}
