package com.xhtt.hiddendangermaster.util.uploadfile;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.bean.file.AppFile;
import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.util.file.FileUtils2;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.hg.hollowgoods.util.xutils.XUtils2;
import com.hg.hollowgoods.util.xutils.callback.base.UploadListener;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.constant.ParamKey;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-03.
 */
public class UploadFileUtils {

    private List<AppFile> result = new ArrayList<>();
    private List<AppFile> needUploadFiles = new ArrayList<>();
    private OnUploadProgressListener onUploadProgressListener;
    private Context context;
    private String requestCode;

    public UploadFileUtils(Context context) {
        this.context = context;
    }

    public void doUpload(List<AppFile> files) {
        doUpload("", files);
    }

    public void doUpload(String requestCode, List<AppFile> files) {

        this.requestCode = requestCode;

        if (files == null || files.size() == 0) {
            backData(true);
        } else {
            result.clear();
            needUploadFiles.clear();
            ArrayList<Object> uploadFiles = new ArrayList<>();

            for (AppFile t : files) {
                if (t.getFile() != null) {
                    needUploadFiles.add(t);
                    uploadFiles.add(t.getFile().getAbsolutePath());
                }
                result.add(t);
            }

            if (needUploadFiles.size() == 0) {
                backData(true);
                return;
            }

            RequestParams params = new RequestParams(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.UploadFile.getUrl()));
            params.setMethod(HttpMethod.POST);
            params.addHeader("token", MyApplication.createApplication().getToken());

            new XUtils2.BuilderUploadFile(true)
                    .setContext(context)
                    .setUploadListener(new UploadListener() {
                        @Override
                        public void onUploadSuccess(String strResult) {

                            ResponseInfo responseInfo = new Gson().fromJson(strResult, ResponseInfo.class);

                            if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                                ArrayList<WebFile> tempData = new Gson().fromJson(
                                        new Gson().toJson(responseInfo.getData()),
                                        new TypeToken<ArrayList<WebFile>>() {
                                        }.getType()
                                );

                                if (tempData != null) {
                                    int count = Math.min(needUploadFiles.size(), tempData.size());
                                    int index;
                                    AppFile media;

                                    for (int i = 0; i < count; i++) {
                                        media = webFile2AppFile(tempData.get(i));
                                        media.setTag(needUploadFiles.get(i).getTag());

                                        index = result.indexOf(needUploadFiles.get(i));
                                        if (index != -1) {
                                            result.set(index, media);
                                        }
                                    }
                                }

                                backData(true);
                            } else {
                                if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                                    t.error("授权已过期，请重新登录");
                                    HGEvent event = new HGEvent(EventActionCode.TokenOverdue);
                                    EventBus.getDefault().post(event);
                                } else {
                                    backData(false);
                                }
                            }
                        }

                        @Override
                        public void onUploadError(Throwable throwable) {
                            backData(false);
                        }

                        @Override
                        public void onUploadLoading(long total, long current) {
                            if (onUploadProgressListener != null) {
                                onUploadProgressListener.progress(total, current);
                            }
                        }

                        @Override
                        public void onUploadFinish() {

                        }

                        @Override
                        public void onUploadCancel(Callback.CancelledException e) {

                        }
                    }).uploadFileDefault(params, uploadFiles);
        }
    }

    private void backData(boolean status) {

        long delayTime = 0;

        if (needUploadFiles.size() == 0) {
            delayTime = 1000;
        }

        new Handler().postDelayed(() -> {

            HGEvent event = new HGEvent(EventActionCode.UPLOAD_PHOTO);
            event.addObj(HGParamKey.RequestCode, requestCode);
            event.addObj(ParamKey.Status, status);
            event.addObj(ParamKey.BackData, result);
            EventBus.getDefault().post(event);
        }, delayTime);
    }

    public void setOnUploadProgressListener(OnUploadProgressListener onUploadProgressListener) {
        this.onUploadProgressListener = onUploadProgressListener;
    }

    public static List<AppFile> webFiles2AppFiles(List<WebFile> webFiles) {

        List<AppFile> result = new ArrayList<>();

        if (webFiles != null) {
            AppFile media;

            for (WebFile t : webFiles) {
                media = webFile2AppFile(t);
                if (media != null) {
                    result.add(media);
                }
            }
        }

        return result;
    }

    public static AppFile webFile2AppFile(WebFile webFile) {

        AppFile result = null;

        if (webFile != null) {
            result = new AppFile();
            result.setOldName(webFile.getFileOldName());
            result.setOldUrl(webFile.getFileSaveName());

            if (FileUtils2.isImageFile(webFile.getFileOldName())) {
                result.setUrl(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.ShowFile.getUrl()) + webFile.getFileSaveName());
            } else {
                result.setUrl(IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.DownloadFile.getUrl()) + webFile.getFileSaveName());
            }
        }

        return result;
    }

    public static ArrayList<WebFile> appFiles2WebFiles(List<AppFile> appFiles) {

        ArrayList<WebFile> result = new ArrayList<>();

        if (appFiles != null) {
            WebFile webFile;

            for (AppFile t : appFiles) {
                webFile = appFile2WebFile(t);

                if (webFile != null) {
                    result.add(webFile);
                }
            }
        }

        return result;
    }

    public static WebFile appFile2WebFile(AppFile appFile) {

        WebFile result = null;

        if (appFile != null && !TextUtils.isEmpty(appFile.getUrl())) {
            result = new WebFile();
            result.setFileOldName(appFile.getOldName());
            result.setFileSaveName(appFile.getOldUrl());
        }

        return result;
    }

}
