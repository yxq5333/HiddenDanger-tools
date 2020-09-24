package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 隐患列表管理层
 *
 * @author HG
 */

public class HiddenDangerListPresenter extends BasePresenter<HiddenDangerListContract.View, HiddenDangerListContract.Model> implements HiddenDangerListContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new HiddenDangerListModel(mView);
    }

    @Override
    public void getData(String companyName, int pageIndex, int pageSize, String key, Integer status, Long checkItemId, Long serviceId) {

        Map<String, Object> request = new HashMap<>();
        request.put("companyName", companyName);
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("riskDescription", key);
        request.put("status", status);
        request.put("checkItemId", checkItemId);
        request.put("serviceId", serviceId);

        mModel.getData(request);
    }

    @Override
    public void getStoreData(int pageIndex, int pageSize, String key, boolean isOnlyMine) {

        Map<String, Object> request = new HashMap<>();
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("riskDescription", key);
        request.put("my", isOnlyMine ? 1 : 0);

        if (isOnlyMine) {
            mModel.getStoreData2(request);
        } else {
            mModel.getStoreData(request);
        }
    }

    @Override
    public void deleteData(long id) {

        ArrayList<Long> request = new ArrayList<>();
        request.add(id);

        mModel.deleteData(request);
    }

    @Override
    public void submitService(long serviceId) {

        Map<String, Object> request = new HashMap<>();
        request.put("id", serviceId);

        mModel.submitService(request);
    }
}
