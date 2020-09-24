package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import com.hg.hollowgoods.UI.Base.MVP.IBaseModel;
import com.hg.hollowgoods.UI.Base.MVP.IBaseView;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;

import java.util.ArrayList;
import java.util.Map;

/**
 * 隐患列表协议层
 *
 * @author HG
 */

public class HiddenDangerListContract {

    public interface Model extends IBaseModel {
        void getData(Map<String, Object> request);

        void getStoreData(Map<String, Object> request);

        void getStoreData2(Map<String, Object> request);

        void deleteData(ArrayList<Long> request);

        void submitService(Map<String, Object> request);
    }

    public interface View extends IBaseView {
        default void getDataSuccess(ArrayList<HiddenDanger> tempData) {

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

        default void submitServiceSuccess(long newServiceId) {
        }

        default void submitServiceError() {
        }

        default void submitServiceFinish() {
        }
    }

    public interface Presenter {
        void getData(String companyName, int pageIndex, int pageSize, String key, Integer status, Long checkItemId, Long serviceId);

        void getStoreData(int pageIndex, int pageSize, String key, boolean isOnlyMine);

        void deleteData(long id);

        void submitService(long serviceId);
    }

}
