package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDangerDetailRequest;
import com.xhtt.hiddendangermaster.constant.WorkType;

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
