package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.IBaseModel;
import com.hg.hollowgoods.UI.Base.MVP.IBaseView;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CheckTable;

import java.util.ArrayList;
import java.util.Map;

/**
 * 检查表协议层
 *
 * @author HG
 */

public class CheckTableListContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);

        void deleteData(ArrayList<Long> request);
    }

    public interface View extends IBaseView {
        default void getDataSuccess(ArrayList<CheckTable> tempData) {

        }

        default void getDataError() {

        }

        default void getDataFinish() {

        }

        default void deleteDataSuccess() {

        }

        default void deleteDataError() {

        }

        default void deleteDataFinish() {

        }
    }

    public interface Presenter {
        void getData(long companyId, int pageIndex, int pageSize, long serviceId, String key);

        void deleteData(long id);
    }

}
