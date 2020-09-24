package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CheckTableContent;

import java.util.ArrayList;
import java.util.Map;

/**
 * 检查内容协议层
 *
 * @author HG
 */

public class CheckTableDetailContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);

        void changeContentStatus(Map<String, Object> request);

        void submitData(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<CheckTableContent> tempData);

        void getDataError();

        void getDataFinish();

        void changeContentStatusSuccess();

        void submitDataSuccess();

        void submitDataError();

        void submitDataFinish();
    }

    public interface Presenter {
        void getData(long checkTableId);

        void changeContentStatus(long checkTableId, long checkContentId, int status);

        void submitData(long checkTableId);
    }

}
