package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;
import com.hg.hollowgoods.util.StringUtils;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDangerDetailRequest;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.util.uploadfile.UploadFileUtils;

/**
 * 隐患排查管理层
 *
 * @author HG
 */

public class HiddenDangerDetailPresenter extends BasePresenter<HiddenDangerDetailContract.View, HiddenDangerDetailContract.Model> implements HiddenDangerDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new HiddenDangerDetailModel(mView);
    }

    @Override
    public void submitData(HiddenDanger hiddenDanger, Company company, WorkType workType, long lastServiceId) {

        Integer hiddenLevel = null;
        if (hiddenDanger.getLevel() != null) {
            hiddenLevel = hiddenDanger.getLevel();
        }

        Long id = null;
        if (workType == WorkType.Edit || workType == WorkType.Change) {
            id = hiddenDanger.getId();
        }

        HiddenDangerDetailRequest request = new HiddenDangerDetailRequest(
                hiddenDanger.getServiceId() == null || hiddenDanger.getServiceId() == 0 ? company.getServiceId() : hiddenDanger.getServiceId(),
                id,
                hiddenDanger.getCompanyId() == 0 ? (company.getId() == 0 ? null : company.getId()) : hiddenDanger.getCompanyId(),
                StringUtils.getDateTimeString(hiddenDanger.getCheckDateShow(), StringUtils.DateFormatMode.LINE_YMD),
                hiddenDanger.getHiddenLocation(),
                UploadFileUtils.appFiles2WebFiles(hiddenDanger.getAppHiddenPhotoList()),
                hiddenDanger.getHiddenDescribe(),
                hiddenLevel,
                hiddenDanger.getReference(),
                hiddenDanger.getChangeFunction(),
                UploadFileUtils.appFiles2WebFiles(hiddenDanger.getAppChangePhotoList()),
                hiddenDanger.getChangeDescribe(),
                hiddenDanger.getStatus(),
                company.getId() == 0 ? company.getCompanyName() : null,
                company.getCheckItemId(),
                lastServiceId
        );

        if (workType == WorkType.Add || workType == WorkType.AddFreeTake) {
            mModel.addData(request);
        } else if (workType == WorkType.Edit) {
            mModel.editData(request);
        } else if (workType == WorkType.Change) {
            mModel.submitData(request);
        }
    }

    @Override
    public void getUserData() {
        mModel.getUserData();
    }
}
