package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.ServiceSubmit;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.ServiceSubmitStatistics;

import java.util.Map;

/**
 * 服务提交协议层
 * <p>
 * Created by Hollow Goods on 2019-05-22
 */

public class ServiceSubmitContract {

    public interface Model extends IBaseModel {
        void getData(long serviceId);

        void submitService(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ServiceSubmitStatistics tempData);

        void getDataError();

        void getDataFinish();

        void submitServiceSuccess(long newServiceId);

        void submitServiceError();

        void submitServiceFinish();
    }

    public interface Presenter {
        void getData(long serviceId);

        void submitService(long serviceId, int dayCount, ServiceSubmit serviceSubmit);
    }

}
