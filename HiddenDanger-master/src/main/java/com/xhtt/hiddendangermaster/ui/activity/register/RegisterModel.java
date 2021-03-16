package com.xhtt.hiddendangermaster.ui.activity.register;

import com.google.gson.Gson;
import com.hg.zero.net.ZxUtils3;
import com.hg.zero.net.callback.base.ZRequestDataListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.RegisterRequest;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

/**
 * 注册数据层
 *
 * @author HG
 */
public class RegisterModel implements RegisterContract.Model {

    private RegisterContract.View mView;

    public RegisterModel(RegisterContract.View mView) {
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
    public void doRegister(RegisterRequest request) {

        if (isViewAttached()) {
            mView.doRegisterStart();
        }

        RequestParams params = new RequestParams(ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.Register.getUrl()));
        params.setMethod(HttpMethod.POST);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(request));

        new ZxUtils3.RequestDataBuilder().setRequestDataListener(new ZRequestDataListener() {
            @Override
            public void onGetSuccess(String result) {
                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (isViewAttached()) {
                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        mView.doRegisterSuccess();
                    } else {
                        Zt.error(responseInfo.getMsg());
                        mView.doRegisterError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable throwable) {
                if (isViewAttached()) {
                    Zt.error(R.string.network_error);
                    mView.doRegisterError();
                }
            }

            @Override
            public void onGetLoading(long l, long l1) {

            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {
                    mView.doRegisterFinish();
                }
            }

            @Override
            public void onGetCancel(Callback.CancelledException e) {

            }
        }).requestData(params);
    }
}
