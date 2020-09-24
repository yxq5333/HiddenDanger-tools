package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.accidentcase;

import com.hg.hollowgoods.ui.base.mvp.IBaseModel;
import com.hg.hollowgoods.ui.base.mvp.IBaseView;
import com.xhtt.hiddendangermaster.bean.knowledgebase.accidentcase.AccidentCase;

import java.util.ArrayList;
import java.util.Map;

/**
 * 主界面协议层
 *
 * @author HG
 */

public class AccidentCaseContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);

        void getHotData(Map<String, Object> request);

        void addReadCount(long id);
    }

    public interface View extends IBaseView {
        void getDataSuccess(ArrayList<AccidentCase> tempData);

        void getDataError();

        void getDataFinish();

        default void getDataNoMore() {

        }

        default void getHotDataSuccess(ArrayList<AccidentCase> tempData) {

        }

        default void getHotDataError() {

        }

        default void getHotDataFinish() {

        }
    }

    public interface Presenter {
        void getData(int pageIndex, int pageSize, String key);

        void getHotData(int pageIndex, int pageSize, String key);

        void addReadCount(long id);
    }

}
