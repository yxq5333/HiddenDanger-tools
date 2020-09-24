package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.IBaseModel;
import com.hg.hollowgoods.UI.Base.MVP.IBaseView;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDangerDetailRequest;
import com.xhtt.hiddendanger.Constant.WorkType;

/**
 * 隐患排查协议层
 *
 * @author HG
 */

public class HiddenDangerDetailContract {

    public interface Model extends IBaseModel {
        void addData(HiddenDangerDetailRequest request);

        void editData(HiddenDangerDetailRequest request);

        void submitData(HiddenDangerDetailRequest request);

        void getUserData();
    }

    public interface View extends IBaseView {
        default void submitDataSuccess(Long id) {
        }

        default void submitDataError() {
        }

        default void submitDataFinish() {
        }

        default void getUserDataSuccess(User user) {

        }
    }

    public interface Presenter {
        void submitData(HiddenDanger hiddenDanger, Company company, WorkType workType, long lastServiceId);

        void getUserData();
    }

}
