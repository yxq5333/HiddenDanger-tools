package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务记录管理层
 *
 * @author HG
 */

public class RecordListPresenter extends ZBasePresenter<RecordListContract.View, RecordListContract.Model> implements RecordListContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new RecordListModel(mView);
    }

    @Override
    public void getData(long companyId, int pageIndex, int pageSize) {

        Map<String, Object> request = new HashMap<>();
        request.put("companyId", companyId);
        request.put("page", pageIndex);
        request.put("limit", pageSize);
        request.put("name", "");

        mModel.getData(request);
    }

}
