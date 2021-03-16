package com.xhtt.hiddendangermaster.ui.fragment.companymap.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZSystemConfig;
import com.hg.zero.net.ZRequestParamsBuilder;
import com.hg.zero.net.ZxUtils3;
import com.hg.zero.net.callback.base.ZDownloadListener;
import com.hg.zero.net.callback.base.ZRequestDataListener;
import com.hg.zero.toast.Zt;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.CompanyMap;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.ui.fragment.companymap.contract.CompanyMapContract;
import com.xhtt.hiddendangermaster.util.LocationChangeUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业地图数据层
 * <p>
 * Created by Hollow Goods on 2020-04-01
 */

public class CompanyMapModel implements CompanyMapContract.Model {

    private CompanyMapContract.View mView;
    private Context mContext;

    public CompanyMapModel(CompanyMapContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
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
    public void getData() {

        Map<String, String> header = new HashMap<>();
        header.put("token", MyApplication.createApplication().getToken());

        RequestParams params = ZRequestParamsBuilder.buildKeyValueRequestParam(
                HttpMethod.GET,
                ZRequestParamsBuilder.buildRequestUrl(InterfaceApi.GetCompanyMapList.getUrl()),
                header,
                null
        );

        new ZxUtils3.RequestDataBuilder().setRequestDataListener(new ZRequestDataListener() {
            @Override
            public void onGetSuccess(String result) {
                if (isViewAttached()) {
                    ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        new Thread(() -> {
                            ArrayList<CompanyMap> tempData = new Gson().fromJson(
                                    new Gson().toJson(responseInfo.getData()),
                                    new TypeToken<ArrayList<CompanyMap>>() {
                                    }.getType()
                            );

                            if (tempData != null) {
                                for (CompanyMap t : tempData) {
                                    t.setServiceTotalCount(3);
                                    if (t.getLat() != null && t.getLng() != null) {
                                        LatLng p = new LatLng(t.getLat(), t.getLng());
                                        p = LocationChangeUtils.BaiDu2GaoDe(mContext, p);
                                        t.setLat(p.latitude + "");
                                        t.setLng(p.longitude + "");
                                    }
                                }
                            }

                            new Handler(Looper.getMainLooper()).post(() -> {
                                mView.getDataSuccess(tempData);
                                mView.getDataFinish();
                            });
                        }).start();
                    } else {
                        if (responseInfo.getCode() == ResponseInfo.CODE_FAIL) {
                            Zt.error(responseInfo.getMsg());
                        } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                            Zt.error("授权已过期，请重新登录");
                            ZEvent event = new ZEvent(EventActionCode.TokenOverdue);
                            EventBus.getDefault().post(event);
                        } else {
                            Zt.error(R.string.network_error);
                        }

                        mView.getDataError();
                        mView.getDataFinish();
                    }
                }
            }

            @Override
            public void onGetError(Throwable ex) {
                if (isViewAttached()) {
                    Zt.error(R.string.network_error);
                    mView.getDataError();
                    mView.getDataFinish();
                }
            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {

                }
            }
        }).requestData(params);

//        ArrayList<CompanyMap> tempData = new ArrayList<>();
//        CompanyMap companyMap;
//
//
//        companyMap = new CompanyMap()
//                .setCompanyId(1)
//                .setLat(31.703031D)
//                .setLng(119.957879D)
//                .setCompanyName("企业")
//                .setCompanyAddress("江苏省常州市武进区湖塘镇马路牙子")
//                .setServiceNowCount(2)
//                .setServiceTotalCount(3);
//        tempData.add(companyMap);

//        for (int i = 0; i < 12; i++) {
//            companyMap = new CompanyMap()
//                    .setCompanyId(i)
//                    .setLat(31.6824D + i * 0.0001)
//                    .setLng(119.96812D + i * 0.0001)
//                    .setCompanyName("企业" + i)
//                    .setCompanyAddress("江苏省常州市武进区湖塘镇马路牙子" + i)
//                    .setServiceNowCount(i % 4)
//                    .setServiceTotalCount(3);
//            tempData.add(companyMap);
//        }

//        mView.getDataSuccess(tempData);
    }

    @Override
    public void addServiceCount(Object companyId) {

        Map<String, String> header = new HashMap<>();
        header.put("token", MyApplication.createApplication().getToken());

        RequestParams params = ZRequestParamsBuilder.buildKeyValueRequestParam(
                ZRequestParamsBuilder.buildRequestUrl(InterfaceApi.AddServiceCount.getUrl()
                        + "/" + companyId
                ),
                header,
                null
        );

        new ZxUtils3.RequestDataBuilder().setRequestDataListener(new ZRequestDataListener() {
            @Override
            public void onGetSuccess(String result) {
                if (isViewAttached()) {
                    ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                    if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                        mView.addServiceCountSuccess();
                    } else {
                        if (responseInfo.getCode() == ResponseInfo.CODE_FAIL) {
                            Zt.error(responseInfo.getMsg());
                        } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                            Zt.error("授权已过期，请重新登录");
                            ZEvent event = new ZEvent(EventActionCode.TokenOverdue);
                            EventBus.getDefault().post(event);
                        } else {
                            Zt.error(R.string.network_error);
                        }

                        mView.addServiceCountError();
                    }
                }
            }

            @Override
            public void onGetError(Throwable ex) {
                if (isViewAttached()) {
                    Zt.error(R.string.network_error);
                    mView.getDataError();
                }
            }

            @Override
            public void onGetFinish() {
                if (isViewAttached()) {
                    mView.addServiceCountFinish();
                }
            }
        }).requestData(params);
    }

    @Override
    public void export() {

        Map<String, String> header = new HashMap<>();
        header.put("token", MyApplication.createApplication().getToken());

        RequestParams params = ZRequestParamsBuilder.buildKeyValueRequestParam(
                ZRequestParamsBuilder.buildRequestUrl(InterfaceApi.Export.getUrl()),
                header,
                null
        );

        params.addParameter("isServiced", true);

        new ZxUtils3.DownloadBuilder().setDownloadListener(new ZDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (isViewAttached()) {
                    mView.exportSuccess(file);
                }
            }

            @Override
            public void onDownloadError(Throwable ex) {
                if (isViewAttached()) {
                    Zt.error(R.string.network_error);
                    mView.exportError();
                }
            }

            @Override
            public void onDownloadLoading(long total, long current) {

            }

            @Override
            public void onDownloadFinish() {
                if (isViewAttached()) {
                    mView.exportFinish();
                }
            }
        }).downloadFile(params, ZSystemConfig.downloadDir());
    }
}
