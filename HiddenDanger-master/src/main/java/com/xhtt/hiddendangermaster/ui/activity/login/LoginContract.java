package com.xhtt.hiddendangermaster.ui.activity.login;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.LoginRequest;

/**
 * 主界面协议层
 *
 * @author HG
 */

public class LoginContract {

    public interface Model extends IBaseModel {
        void doLogin(LoginRequest request);
    }

    public interface View extends IBaseView {
        void doLoginStart();

        void doLoginSuccess(String token);

        void doLoginError();

        void doLoginFinish();
    }

    public interface Presenter {
        void doLogin(String username, String password);
    }

}
