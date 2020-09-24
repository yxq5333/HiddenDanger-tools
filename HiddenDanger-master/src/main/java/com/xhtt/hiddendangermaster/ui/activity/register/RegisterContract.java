package com.xhtt.hiddendangermaster.ui.activity.register;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.RegisterRequest;

/**
 * 注册协议层
 *
 * @author HG
 */
public class RegisterContract {

    public interface Model extends IBaseModel {
        void doRegister(RegisterRequest request);
    }

    public interface View extends IBaseView {
        void doRegisterStart();

        void doRegisterSuccess();

        void doRegisterError();

        void doRegisterFinish();
    }

    public interface Presenter {
        void doRegister(String phone, String name, String username, String inputCode, String realCode, String password);
    }

}
