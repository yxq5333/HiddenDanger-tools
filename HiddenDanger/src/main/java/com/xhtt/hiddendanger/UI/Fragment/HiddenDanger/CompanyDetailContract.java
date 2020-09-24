package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.IBaseModel;
import com.hg.hollowgoods.UI.Base.MVP.IBaseView;
import com.xhtt.hiddendanger.Bean.Common.CommonChooseItem;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CompanyDetailRequest;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CompanyOnlyName;
import com.xhtt.hiddendanger.Constant.WorkType;

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
