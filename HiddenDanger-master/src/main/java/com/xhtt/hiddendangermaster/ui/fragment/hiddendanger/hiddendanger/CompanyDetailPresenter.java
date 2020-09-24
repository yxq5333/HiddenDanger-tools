package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.CommonChooseItem;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CompanyDetailRequest;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CompanyOnlyName;
import com.xhtt.hiddendangermaster.constant.WorkType;

/**
 * 企业基本信息管理层
 *
 * @author HG
 */

public class CompanyDetailPresenter extends BasePresenter<CompanyDetailContract.View, CompanyDetailContract.Model> implements CompanyDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new CompanyDetailModel(mView);
    }

    @Override
    public void submitData(
            Company company,
            CommonChooseItem province,
            CommonChooseItem city,
            CommonChooseItem distract,
            CommonChooseItem town,
            WorkType workType) {

        CompanyDetailRequest request = new CompanyDetailRequest(
                workType == WorkType.Edit ? company.getId() : null,
                company.getCompanyName(),
                company.getAddress(),
                company.getMainPeople(),
                company.getMainPeoplePhone(),
                company.getBusiness(),
                company.getProportion(),
                company.getPeopleCount(),
                province == null ? null : province.getId(),
                city == null ? null : city.getId(),
                distract == null ? null : distract.getId(),
                town == null ? null : town.getId()
        );

        if (workType == WorkType.Add) {
            mModel.addData(request);
        } else if (workType == WorkType.Edit) {
            mModel.editData(request);
        }
    }

    @Override
    public void submitData(CompanyOnlyName company, WorkType workType) {

        CompanyDetailRequest request = new CompanyDetailRequest(
                workType == WorkType.Edit ? company.getId() : null,
                company.getCompanyName(),
                company.getAddress(),
                company.getMainPeople(),
                company.getMainPeoplePhone(),
                company.getBusiness(),
                company.getProportion(),
                company.getPeopleCount(),
                null,
                null,
                null,
                null
        );

        if (workType == WorkType.Add) {
            mModel.addData(request);
        } else if (workType == WorkType.Edit) {
            mModel.editData(request);
        }
    }
}
