package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.BasePresenter;
import com.hg.hollowgoods.Util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 检查内容管理层
 *
 * @author HG
 */

public class CheckTableDetailPresenter extends BasePresenter<CheckTableDetailContract.View, CheckTableDetailContract.Model> implements CheckTableDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new CheckTableDetailModel(mView);
    }

    @Override
    public void getData(long checkTableId) {

        Map<String, Object> request = new HashMap<>();
        request.put("checkTableId", checkTableId);

        mModel.getData(request);
    }

    @Override
    public void changeContentStatus(long checkTableId, long checkContentId, int status) {

        Map<String, Object> request = new HashMap<>();
        request.put("checkTableId", checkTableId);
        request.put("id", checkContentId);
        request.put("status", status);

        mModel.changeContentStatus(request);
    }

    @Override
    public void submitData(long checkTableId) {

        Map<String, Object> request = new HashMap<>();
        request.put("id", checkTableId);
        request.put("checkDate", StringUtils.getDateTimeString(System.currentTimeMillis(), StringUtils.DateFormatMode.LINE_YMD));

        mModel.submitData(request);
    }
}
