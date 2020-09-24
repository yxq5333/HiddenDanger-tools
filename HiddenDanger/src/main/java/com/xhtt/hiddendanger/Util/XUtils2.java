package com.xhtt.hiddendanger.Util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.hg.hollowgoods.Application.ApplicationBuilder;
import com.hg.hollowgoods.Bean.CommonBean.KeyValue;
import com.hg.hollowgoods.Constant.HGSystemConfig;
import com.hg.hollowgoods.R;
import com.hg.hollowgoods.Util.EncryptUtils;
import com.hg.hollowgoods.Util.FileUtils;
import com.hg.hollowgoods.Util.FormatUtils;
import com.hg.hollowgoods.Util.LogUtils;
import com.hg.hollowgoods.Util.ThreadPoolUtils;
import com.hg.hollowgoods.Util.XUtils.DownloadListener;
import com.hg.hollowgoods.Util.XUtils.GetHttpDataListener;
import com.hg.hollowgoods.Util.XUtils.LoadImageListener;
import com.hg.hollowgoods.Util.XUtils.LoadImageOptions;
import com.hg.hollowgoods.Util.XUtils.UploadListener;
import com.hg.hollowgoods.Util.XUtils.XUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * XUtils工具类第二代
 * <p>
 * 改动点：改为链式调用
 * <p>
 * Created by Hollow Goods on unknown.
 */
public class XUtils2 {

    /**
     * 初始化，XUtils和XUtils2的初始化方法随便调哪个
     *
     * @param application application
     */
    public static void init(Application application) {
        XUtils.init(application);
    }

    /**
     * 加载图片构建者
     */
    public static class BuilderLoadImage {

        /**
         * 获取图片监听
         **/
        private LoadImageListener loadImageListener = null;

        /**
         * 设置获取图片监听
         *
         * @param loadImageListener loadImageListener
         */
        public BuilderLoadImage setLoadImageListener(LoadImageListener loadImageListener) {
            this.loadImageListener = loadImageListener;
            return this;
        }

        /**
         * 获取图片
         *
         * @param loadImageOptions 获取图片配置
         */
        public void loadImageByUrl(LoadImageOptions loadImageOptions) {

            ImageOptions imageOptions = new ImageOptions.Builder()//
                    .setSize(DensityUtil.dip2px(loadImageOptions.widthDp),
                            DensityUtil.dip2px(loadImageOptions.heightDp))// 图片大小
                    .setRadius(DensityUtil.dip2px(loadImageOptions.radiusDp))// ImageView圆角半径
                    .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setImageScaleType(loadImageOptions.scaleType)//
                    .setLoadingDrawableId(loadImageOptions.loadIconRes)// 加载中默认显示图片
                    .setFailureDrawableId(loadImageOptions.failIconRes)// 加载失败后默认显示图片
                    .setFadeIn(loadImageOptions.isFadeIn)// 加载图片完成后，显示图片时是否渐变显示
                    .setCircular(loadImageOptions.isCircular)// 是否显示圆形图片
                    .build();

            // TODO 缓存地址
            String cachePath = HGSystemConfig.getPhotoCachePath();
            // 缓存名
            String cacheName = EncryptUtils.md5Encrypt(loadImageOptions.url + loadImageOptions.widthDp + loadImageOptions.heightDp);
            // 是否从网络加载
            boolean isFromNet = true;

            // 判断图片是否有缓存
            // 有缓存则从本地加载
            // 否则从网络加载
            File file = new File(cachePath, cacheName);
            if (file.exists()) {
                loadImageOptions.url = cachePath + cacheName;
                isFromNet = false;
            }

            if (!loadImageOptions.url.startsWith("http:")) {
                isFromNet = false;
            }

            boolean isPng;
            if (loadImageOptions.url.contains("png") || loadImageOptions.url.contains("PNG")) {
                isPng = true;
            } else {
                isPng = false;
            }

            // 判断要加载的控件是否为ImageView
            if (loadImageOptions.view == null || !(loadImageOptions.view instanceof ImageView)) {
                x.image().loadDrawable(loadImageOptions.url, imageOptions,
                        new LoadImageCallBack(loadImageOptions.url, isFromNet, cachePath, cacheName, loadImageOptions.view, isPng));
            } else {
                x.image().bind((ImageView) (loadImageOptions.view), loadImageOptions.url, imageOptions,
                        new LoadImageCallBack(loadImageOptions.url, isFromNet, cachePath, cacheName, loadImageOptions.view, isPng));
            }
        }


        /**
         * 获取图片返回结果
         * <p>
         * Created by Hollow Goods on unknown.
         */
        private class LoadImageCallBack implements Callback.ProgressCallback<Drawable> {

            private String url;
            private boolean isFromNet;
            private String cachePath;
            private String cacheName;
            private View view;
            private boolean isPng;

            LoadImageCallBack(String url, boolean isFromNet, String cachePath, String cacheName, View view, boolean isPng) {
                this.url = url;
                this.isFromNet = isFromNet;
                this.cachePath = cachePath;
                this.cacheName = cacheName;
                this.view = view;
                this.isPng = isPng;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.LogRequest(
                        new RequestParams(url),
                        cex == null ? "" : cex.getMessage()
                );
                if (loadImageListener != null) {
                    loadImageListener.onLoadCancel(view, cex);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.LogRequest(
                        new RequestParams(url),
                        ex == null ? "" : ex.getMessage()
                );
                if (loadImageListener != null) {
                    loadImageListener.onLoadError(view, ex);
                }
            }

            @Override
            public void onFinished() {
                if (loadImageListener != null) {
                    loadImageListener.onLoadFinish(view);
                }
            }

            @Override
            public void onSuccess(Drawable drawable) {
                if (isFromNet && drawable != null) {// 从网络获取图片
                    BitmapDrawable bd = (BitmapDrawable) drawable;
                    Bitmap bitmap = bd.getBitmap();

                    if (loadImageListener != null) {
                        loadImageListener.onLoadSuccess(view, bitmap);
                    }

                    ThreadPoolUtils.getService().execute(() -> FormatUtils.savePhoto(bitmap, cachePath, cacheName, 100, isPng));
                } else {// 从本地获取图片
                    if (loadImageListener != null) {
                        BitmapDrawable bd = (BitmapDrawable) drawable;
                        Bitmap bitmap = bd.getBitmap();

                        loadImageListener.onLoadSuccess(view, bitmap);
                    }
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isLoading) {
                if (loadImageListener != null) {
                    loadImageListener.onLoadLoading(view, total, current);
                }
            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }
        }
    }

    /**
     * 获取数据构建者
     */
    public static class BuilderGetHttpData {

        /**
         * 获取网络数据监听
         **/
        private GetHttpDataListener getHttpDataListener = null;
        private Callback.Cancelable getHttpCancelable = null;

        /**
         * 设置获取网络数据监听
         *
         * @param getHttpDataListener getHttpDataListener
         */
        public BuilderGetHttpData setGetHttpDataListener(GetHttpDataListener getHttpDataListener) {
            this.getHttpDataListener = getHttpDataListener;
            return this;
        }

        /**
         * 获取网络数据
         *
         * @param params 请求参数
         */
        public void getHttpData(RequestParams params) {

            String url = params.toString();
            LogUtils.LogRequest(params, "开始请求……");

            if (TextUtils.isEmpty(url)) {
                if (getHttpDataListener != null) {
                    Application application = ApplicationBuilder.create();
                    getHttpDataListener.onGetError(new Throwable(application.getString(R.string.no_set_url)));
                }
            } else {
                cancelGetHttpData();
                // TODO 设置连接超时时间
                params.setConnectTimeout(HGSystemConfig.TIME_OUT);
                getHttpCancelable = x.http().request(params.getMethod(), params, new GetHttpCallBack(params));
            }
        }

        /**
         * 取消获取网络数据
         */
        public void cancelGetHttpData() {
            if (getHttpCancelable != null) {
                getHttpCancelable.cancel();
                getHttpCancelable = null;
            }
        }

        /**
         * 获取网络数据返回结果
         * <p>
         * Created by Hollow Goods on unknown.
         */
        private class GetHttpCallBack implements Callback.ProgressCallback<String> {

            private RequestParams params;

            GetHttpCallBack(RequestParams params) {
                this.params = params;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.LogRequest(
                        params,
                        cex == null ? "" : cex.getMessage()
                );
                if (getHttpDataListener != null) {
                    getHttpDataListener.onGetCancel(cex);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.LogRequest(
                        params,
                        ex == null ? "" : ex.getMessage()
                );
                if (getHttpDataListener != null) {
                    getHttpDataListener.onGetError(ex);
                }
            }

            @Override
            public void onFinished() {
                if (getHttpDataListener != null) {
                    getHttpDataListener.onGetFinish();
                }
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.LogRequest(
                        params,
                        result
                );
                if (getHttpDataListener != null) {
                    getHttpDataListener.onGetSuccess(result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isLoading) {
                if (getHttpDataListener != null) {
                    getHttpDataListener.onGetLoading(total, current);
                }
            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }

        }
    }

    /**
     * 下载文件构建者
     */
    public static class BuilderDownloadFile {

        /**
         * 下载文件监听
         **/
        private DownloadListener downloadListener = null;
        private Callback.Cancelable downloadCancelable = null;

        /**
         * 设置下载文件监听
         *
         * @param downloadListener downloadListener
         */
        public BuilderDownloadFile setDownloadListener(DownloadListener downloadListener) {
            this.downloadListener = downloadListener;
            return this;
        }

        /**
         * 下载文件
         *
         * @param params 请求参数
         * @param path   保存路径
         */
        public void downloadFile(RequestParams params, String path) {

            String url = params.toString();
            LogUtils.LogRequest(params, "开始请求……");

            if (TextUtils.isEmpty(url)) {
                if (downloadListener != null) {
                    Application application = ApplicationBuilder.create();
                    downloadListener.onDownloadError(new Throwable(application.getString(R.string.no_set_url)));
                }
            } else {
                cancelDownloadFile();
                // TODO 设置连接超时时间
                params.setConnectTimeout(HGSystemConfig.TIME_OUT);

                if (TextUtils.isEmpty(path)) {
                    // 设置下载保存路径
                    params.setSaveFilePath(HGSystemConfig.getDownloadFilePath());
                    // 自动命名
                    params.setAutoRename(true);
                } else {
                    // 设置下载保存路径
                    params.setSaveFilePath(path);
                    // 自动命名
                    params.setAutoRename(false);
                }
                // 自动断点下载
                params.setAutoResume(true);

                downloadCancelable = x.http().request(params.getMethod(), params, new DownloadCallBack(params));
            }
        }

        /**
         * 下载文件
         *
         * @param params 请求参数
         */
        public void downloadFile(RequestParams params) {
            downloadFile(params, "");
        }

        /**
         * 取消下载文件
         */
        public void cancelDownloadFile() {
            if (downloadCancelable != null) {
                downloadCancelable.cancel();
                downloadCancelable = null;
            }
        }

        /**
         * 下载文件返回结果
         * <p>
         * Created by Hollow Goods on unknown.
         */
        private class DownloadCallBack implements Callback.ProgressCallback<File> {

            private RequestParams params;

            DownloadCallBack(RequestParams params) {
                this.params = params;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.LogRequest(
                        params,
                        cex == null ? "" : cex.getMessage()
                );
                if (downloadListener != null) {
                    downloadListener.onDownloadCancel(cex);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.LogRequest(
                        params,
                        ex == null ? "" : ex.getMessage()
                );
                if (downloadListener != null) {
                    downloadListener.onDownloadError(ex);
                }
            }

            @Override
            public void onFinished() {
                if (downloadListener != null) {
                    downloadListener.onDownloadFinish();
                }
            }

            @Override
            public void onSuccess(File file) {
                LogUtils.LogRequest(
                        params,
                        file == null ? "" : file.getAbsolutePath()
                );
                if (downloadListener != null) {
                    downloadListener.onDownloadSuccess(file);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isLoading) {
                LogUtils.LogRequest(
                        params,
                        current + "/" + total
                );
                if (downloadListener != null) {
                    downloadListener.onDownloadLoading(total, current);
                }
            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }
        }
    }

    /**
     * 上传文件构建者
     * <p>
     * 压缩图片使用鲁班库
     */
    public static class BuilderUploadFile {

        /**
         * 上传文件监听
         **/
        private UploadListener uploadListener = null;
        private Callback.Cancelable uploadCancelable = null;
        private boolean isAutoCompress;
        private CountDownLatch countDownLatch;
        private Context context;
        private ArrayList<KeyValue> tempFilePaths;

        /**
         * @param isAutoCompress boolean 是否启动自动压缩
         */
        public BuilderUploadFile(boolean isAutoCompress) {
            this.isAutoCompress = isAutoCompress;
        }

        /**
         * 设置上下文
         *
         * @param context Context
         * @return BuilderUploadFile
         */
        public BuilderUploadFile setContext(Context context) {
            this.context = context;
            return this;
        }

        /**
         * 设置上传文件监听
         *
         * @param uploadListener uploadListener
         */
        public BuilderUploadFile setUploadListener(UploadListener uploadListener) {
            this.uploadListener = uploadListener;
            return this;
        }

        /**
         * 上传文件
         *
         * @param params    params
         * @param filePaths filePaths
         */
        public void uploadFile(RequestParams params, ArrayList<KeyValue> filePaths) {

            if (isAutoCompress && context != null && filePaths != null) {
                ThreadPoolUtils.getService().execute(() -> {
                    countDownLatch = new CountDownLatch(filePaths.size());
                    tempFilePaths = new ArrayList<>(filePaths);
                    List<File> source = new ArrayList<>();

                    for (KeyValue t : filePaths) {
                        source.add(new File(t.getValue() + ""));
                    }

                    // 执行图片压缩的逻辑
                    Luban.with(context)
                            .load(source)
                            .ignoreBy(100)
                            .setTargetDir(HGSystemConfig.getDataCachePath())
                            .setFocusAlpha(true)
                            .filter(FileUtils::isImageFile)
                            .setRenameListener(filePath -> {
                                String ex = filePath.substring(filePath.lastIndexOf("."));
                                return EncryptUtils.md5Encrypt(filePath) + ex;
                            })
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                    // 压缩开始前调用，可以在方法内启动 loading UI
                                }

                                @Override
                                public void onSuccess(File file) {
                                    // 压缩成功后调用，返回压缩后的图片文件
                                    LogUtils.Log("压缩成功",
                                            file.getAbsolutePath()
                                    );
                                    onFinish(file.getAbsolutePath());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // 当压缩过程出现问题时调用
                                    LogUtils.Log("压缩失败", e.getMessage());
                                    onFinish("");
                                }

                                void onFinish(String resultPath) {
                                    // 不管成功与否都调用该方法
                                    if (!TextUtils.isEmpty(resultPath)) {
                                        for (KeyValue t : tempFilePaths) {
                                            String value = t.getValue() + "";
                                            String ex = "";
                                            if (value.lastIndexOf(".") != -1) {
                                                ex = value.substring(value.lastIndexOf("."));
                                            }
                                            String p = HGSystemConfig.getDataCachePath();
                                            String md5 = p + EncryptUtils.md5Encrypt(value) + ex;

                                            if (FileUtils.equals(value, resultPath)
                                                    || FileUtils.equals(md5, resultPath)
                                            ) {
                                                t.setValue(resultPath);
                                                break;
                                            }
                                        }
                                    }

                                    countDownLatch.countDown();
                                }
                            })
                            .launch();

                    try {
                        countDownLatch.await();
                    } catch (InterruptedException ignored) {

                    }

                    doUploadFile(params, tempFilePaths);
                });
            } else {
                doUploadFile(params, filePaths);
            }
        }

        /**
         * 上传文件，默认key(file)
         *
         * @param params    params
         * @param filePaths filePaths
         */
        public void uploadFileDefault(RequestParams params, ArrayList<Object> filePaths) {

            ArrayList<KeyValue> paths = new ArrayList<>();
            // 文件上传默认的key
            String DEFAULT_UPLOAD_FILE_KEY = "file";

            if (filePaths != null) {
                for (Object t : filePaths) {
                    paths.add(new KeyValue(DEFAULT_UPLOAD_FILE_KEY, t));
                }
            }

            uploadFile(params, paths);
        }

        /**
         * 上传图片
         *
         * @param params    RequestParams
         * @param filePaths ArrayList<KeyValue>
         */
        private void doUploadFile(RequestParams params, ArrayList<KeyValue> filePaths) {

            String url = params.toString();
            LogUtils.LogRequest(params, "开始请求……");

            if (TextUtils.isEmpty(url)) {
                if (uploadListener != null) {
                    Application application = ApplicationBuilder.create();
                    uploadListener.onUploadError(new Throwable(application.getString(R.string.no_set_url)));
                }
            } else {
                cancelUploadFile();
                // TODO 设置连接超时时间
                params.setConnectTimeout(HGSystemConfig.TIME_OUT);
                params.setMultipart(true);

                if (filePaths != null) {
                    for (KeyValue t : filePaths) {
                        if (t.getValue() instanceof String) {
                            params.addBodyParameter(t.getKey(), new File((String) t.getValue()));
                        } else if (t.getValue() instanceof File) {
                            params.addBodyParameter(t.getKey(), t.getValue());
                        }
                    }
                }

//                uploadCancelable = x.http().post(params, new UploadCallBack(params));
                uploadCancelable = x.http().request(params.getMethod(), params, new UploadCallBack(params));
            }
        }

        /**
         * 取消下载文件
         */
        public void cancelUploadFile() {
            if (uploadCancelable != null) {
                uploadCancelable.cancel();
                uploadCancelable = null;
            }
        }

        /**
         * 上传文件返回结果
         * <p>
         * Created by Hollow Goods on unknown.
         */
        private class UploadCallBack implements Callback.ProgressCallback<String> {

            private RequestParams params;

            UploadCallBack(RequestParams params) {
                this.params = params;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.LogRequest(
                        params,
                        cex == null ? "" : cex.getMessage()
                );
                if (uploadListener != null) {
                    uploadListener.onUploadCancel(cex);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.LogRequest(
                        params,
                        ex == null ? "" : ex.getMessage()
                );
                if (uploadListener != null) {
                    uploadListener.onUploadError(ex);
                }
            }

            @Override
            public void onFinished() {
                if (uploadListener != null) {
                    uploadListener.onUploadFinish();
                }
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.LogRequest(
                        params,
                        result
                );
                if (uploadListener != null) {
                    uploadListener.onUploadSuccess(result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isLoading) {
                LogUtils.LogRequest(
                        params,
                        current + "/" + total
                );
                if (uploadListener != null) {
                    uploadListener.onUploadLoading(total, current);
                }
            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }

        }
    }

}
