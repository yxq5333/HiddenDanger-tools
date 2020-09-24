package com.xhtt.hiddendangermaster.ui.fragment.profile;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.User;

import java.util.Map;

/**
 * 我的协议层
 *
 * @author HG
 */

public class ProfileContract {

    public interface Model extends IBaseModel {
        void getData();

        void updateUserData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(User user);

        void getDataError();

        void getDataFinish();

        void updateUserDataSuccess();

        void updateUserDataError();
    }

    public interface Presenter {
        void getData();

        void updateUserData(String username, String name);
    }

}
