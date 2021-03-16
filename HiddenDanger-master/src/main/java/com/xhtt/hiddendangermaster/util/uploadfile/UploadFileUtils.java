package com.xhtt.hiddendangermaster.util.uploadfile;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.constant.ZParamKey;
import com.hg.zero.file.ZAppFile;
import com.hg.zero.file.ZFileUtils2;
import com.hg.zero.net.ZxUtils3;
import com.hg.zero.net.callback.base.ZUploadListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
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

    private List<ZAppFile> result = new ArrayList<>();
    private List<ZAppFile> needUploadFiles = new ArrayList<>();
    private OnUploadProgressListener onUploadProgressListener;
    private Context context;
    private String requestCode;

    public UploadFileUtils(Context context) {
        this.context = context;
    }

    public void doUpload(List<ZAppFile> files) {
        doUpload("", files);
    }

    public void doUpload(String requestCode, List<ZAppFile> files) {

        this.requestCode = requestCode;

        if (files == null || files.size() == 0) {
            backData(true);
        } else {
            result.clear();
            needUploadFiles.clear();
            ArrayList<Object> uploadFiles = new ArrayList<>();

            for (ZAppFile t : files) {
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

            RequestParams params = new RequestParams(ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.UploadFile.getUrl()));
            params.setMethod(HttpMethod.POST);
            params.addHeader("token", MyApplication.createApplication().getToken());

            new ZxUtils3.UploadBuilder()
                    .setAutoCompress(true)
                    .setContext(context)
                    .setUploadListener(new ZUploadListener() {
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
                                    ZAppFile media;

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
                                    Zt.error("授权已过期，请重新登录");
                                    ZEvent event = new ZEvent(EventActionCode.TokenOverdue);
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

            ZEvent event = new ZEvent(EventActionCode.UPLOAD_PHOTO);
            event.addObj(ZParamKey.RequestCode, requestCode);
            event.addObj(ParamKey.Status, status);
            event.addObj(ParamKey.BackData, result);
            EventBus.getDefault().post(event);
        }, delayTime);
    }

    public void setOnUploadProgressListener(OnUploadProgressListener onUploadProgressListener) {
        this.onUploadProgressListener = onUploadProgressListener;
    }

    public static List<ZAppFile> webFiles2AppFiles(List<WebFile> webFiles) {

        List<ZAppFile> result = new ArrayList<>();

        if (webFiles != null) {
            ZAppFile media;

            for (WebFile t : webFiles) {
                media = webFile2AppFile(t);
                if (media != null) {
                    result.add(media);
                }
            }
        }

        return result;
    }

    public static ZAppFile webFile2AppFile(WebFile webFile) {

        ZAppFile result = null;

        if (webFile != null) {
            result = new ZAppFile();
            result.setOriginalName(webFile.getFileOldName());
            result.setGenerateName(webFile.getFileSaveName());

            if (ZFileUtils2.isImageFile(webFile.getFileOldName())) {
                result.setUrl(ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.ShowFile.getUrl()) + webFile.getFileSaveName());
            } else {
                result.setUrl(ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.DownloadFile.getUrl()) + webFile.getFileSaveName());
            }
        }

        return result;
    }

    public static ArrayList<WebFile> appFiles2WebFiles(List<ZAppFile> appFiles) {

        ArrayList<WebFile> result = new ArrayList<>();

        if (appFiles != null) {
            WebFile webFile;

            for (ZAppFile t : appFiles) {
                webFile = appFile2WebFile(t);

                if (webFile != null) {
                    result.add(webFile);
                }
            }
        }

        return result;
    }

    public static WebFile appFile2WebFile(ZAppFile appFile) {

        WebFile result = null;

        if (appFile != null && !TextUtils.isEmpty(appFile.getUrl())) {
            result = new WebFile();
            result.setFileOldName(appFile.getOriginalName());
            result.setFileSaveName(appFile.getGenerateName());
        }

        return result;
    }

}
