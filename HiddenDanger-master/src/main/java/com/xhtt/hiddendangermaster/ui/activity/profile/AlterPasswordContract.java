package com.xhtt.hiddendangermaster.ui.activity.profile;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;

import java.util.Map;

/**
 * 修改密码协议层
 *
 * @author HG
 */

public class AlterPasswordContract {

    public interface Model extends IBaseModel {
        void changePassword(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void changePasswordStart();

        void changePasswordSuccess();

        void changePasswordError();

        void changePasswordFinish();
    }

    public interface Presenter {
        void changePassword(String oldPassword, String newPassword, String reNewPassword);
    }

}
