package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.XUtils.GetHttpDataListener;
import com.hg.hollowgoods.Util.XUtils.XUtils;
import com.xhtt.hiddendanger.Application.HiddenDangerApplication;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Record;
import com.xhtt.hiddendanger.Bean.ResponseInfo;
import com.xhtt.hiddendanger.Constant.InterfaceApi;
import com.xhtt.hiddendanger.R;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 服务记录数据层
 *
 * @author HG
 */

public class RecordListModel implements RecordListContract.Model {

    private RecordListContract.View mView;

    public RecordListModel(RecordListContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void getData(Map<String, Object> request) {

        RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.RecordList.getUrl()));
        params.addHeader("token", MyApplication.createApplication().getToken());

        Set<String> set = request.keySet();
        Iterator<String> keys = set.iterator();
        String key;
        Object value;

        while (keys.hasNext()) {
            key = keys.next();
            value = request.get(key);
            if (value != null) {
                params.addParameter(key, value);
            }
        }

        new XUtils2.BuilderGetHttpData()
        xUtils.setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<Record> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getPage().getList()),
                                    new TypeToken<ArrayList<Record>>() {
                                    }.getType()
                            );

                            mView.getDataSuccess(tempData);
                        }).start();
                    } else {
                        if (responseInfo.getCode() == ResponseInfo.CODE_FAIL) {
                            t.error(responseInfo.getMsg());
                        } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                            t.error("授权已过期，请重新登录");
                            HGEvent event = new HGEvent(EventActionCode.TokenOverdue);
                            EventBus.getDefault().post(event);
                        } else {
                            t.error(R.string.network_error);
                        }

                        mView.getDataError();
                        mView.getDataFinish();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    t.error(R.string.network_error);
                    mView.getDataError();
                    mView.getDataFinish();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {

                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        });
        xUtils.getHttpData(HttpMethod.GET, params);
    }

}
