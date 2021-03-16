package com.xhtt.hiddendangermaster.ui.activity.profile;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;

import java.util.Map;

/**
 * 修改密码协议层
 *
 * @author HG
 */

public class AlterPasswordContract {

    public interface Model extends ZIBaseModel {
        void changePassword(Map<String, Object> request);
    }

    public interface View extends ZIBaseView {
        void changePasswordStart();

        void changePasswordSuccess();

        void changePasswordError();

        void changePasswordFinish();
    }

    public interface Presenter {
        void changePassword(String oldPassword, String newPassword, String reNewPassword);
    }

}
