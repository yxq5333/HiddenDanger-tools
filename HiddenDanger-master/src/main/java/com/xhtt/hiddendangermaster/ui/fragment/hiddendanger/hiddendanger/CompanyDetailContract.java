package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.CommonChooseItem;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CompanyDetailRequest;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CompanyOnlyName;
import com.xhtt.hiddendangermaster.constant.WorkType;

/**
 * 企业基本信息协议层
 *
 * @author HG
 */

public class CompanyDetailContract {

    public interface Model extends IBaseModel {
        void addData(CompanyDetailRequest request);

        void editData(CompanyDetailRequest request);
    }

    public interface View extends IBaseView {
        default void submitDataSuccess(Long id, Long serviceId) {
        }

        default void submitDataError() {
        }

        default void submitDataFinish() {
        }
    }

    public interface Presenter {
        void submitData(
                Company company,
                CommonChooseItem province,
                CommonChooseItem city,
                CommonChooseItem distract,
                CommonChooseItem town,
                WorkType workType
        );

        void submitData(CompanyOnlyName company, WorkType workType);
    }

}
