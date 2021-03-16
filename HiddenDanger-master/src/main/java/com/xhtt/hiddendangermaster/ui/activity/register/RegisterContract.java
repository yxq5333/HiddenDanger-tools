package com.xhtt.hiddendangermaster.ui.activity.register;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.RegisterRequest;

/**
 * 注册协议层
 *
 * @author HG
 */
public class RegisterContract {

    public interface Model extends ZIBaseModel {
        void doRegister(RegisterRequest request);
    }

    public interface View extends ZIBaseView {
        void doRegisterStart();

        void doRegisterSuccess();

        void doRegisterError();

        void doRegisterFinish();
    }

    public interface Presenter {
        void doRegister(String phone, String name, String username, String inputCode, String realCode, String password);
    }

}
