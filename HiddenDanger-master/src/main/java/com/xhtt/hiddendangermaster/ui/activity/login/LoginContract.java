package com.xhtt.hiddendangermaster.ui.activity.login;

import com.hg.zero.ui.base.mvp.ZIBaseModel;
import com.hg.zero.ui.base.mvp.ZIBaseView;
import com.xhtt.hiddendangermaster.bean.LoginRequest;

/**
 * 主界面协议层
 *
 * @author HG
 */

public class LoginContract {

    public interface Model extends ZIBaseModel {
        void doLogin(LoginRequest request);
    }

    public interface View extends ZIBaseView {
        void doLoginStart();

        void doLoginSuccess(String token);

        void doLoginError();

        void doLoginFinish();
    }

    public interface Presenter {
        void doLogin(String username, String password);
    }

}
