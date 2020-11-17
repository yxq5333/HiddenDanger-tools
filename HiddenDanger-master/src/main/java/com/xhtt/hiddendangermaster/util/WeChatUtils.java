package com.xhtt.hiddendangermaster.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.util.APPUtils;
import com.hg.hollowgoods.util.FormatUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xhtt.hiddendangermaster.BuildConfig;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;

import java.io.File;

/**
 * Created by Hollow Goods on 2019-07-25.
 */
public class WeChatUtils {

    public static final int SCENE_FRIEND = 1;
    public static final int SCENE_MOMENTS = 2;

    private static WeChatUtils instance;

    private WeChatUtils() {

    }

    public static WeChatUtils create() {

        if (instance == null) {
            instance = new WeChatUtils();
        }

        return instance;
    }

    private IWXAPI api;
    private Context context;

    public WeChatUtils init(Context context) {

        this.context = context;

        api = WXAPIFactory.createWXAPI(context, BuildConfig.WXAndroidKey, true);
        // 将应用的appid注册到微信
        api.registerApp(BuildConfig.WXAndroidKey);

        return instance;
    }

    public void doWeChatLogin() {
        if (api.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "walk_more";
            api.sendReq(req);
        } else {
            t.warning("请先安装微信");
        }
    }

    private int scene;
    private String title;
    private String describe;
    private Object thumbImgUrl;
    private String contentUrl;

    /**
     * 微信分享预备
     *
     * @param scene       int 场景 1 分享给朋友 2 分享到朋友圈
     * @param title       String 标题 限制长度不超过512Bytes
     * @param describe    String 描述 限制长度不超过1KB
     * @param thumbImgUrl String 缩略图地址 限制内容大小不超过32KB
     * @param contentUrl  String 内容地址
     */
    public void preWeChatShare(int scene, String title, String describe, Object thumbImgUrl, String contentUrl) {
        if (api.isWXAppInstalled()) {
            this.scene = scene;
            this.title = title;
            this.describe = describe;
            this.thumbImgUrl = thumbImgUrl;
            this.contentUrl = contentUrl;

            downloadThumbImgFromUrl();
        } else {
            t.warning("请先安装微信");
        }
    }


    private void doWeChatShare(byte[] thumbImgBytes) {

        //初始化一个WXWebPageObject，填写url
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = contentUrl;

        // 用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        msg.description = describe;
        msg.thumbData = thumbImgBytes != null ? thumbImgBytes : FormatUtils.bitmap2Bytes(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo));

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "" + System.currentTimeMillis();
        req.message = msg;
        req.scene = scene == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        // 调用api接口，发送数据到微信
        api.sendReq(req);
    }

    private void downloadThumbImgFromUrl() {

        if (thumbImgUrl instanceof String) {

            Glide.with(context)
                    .asBitmap()
                    .load((String) thumbImgUrl)
                    .into(new CustomTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                doWeChatShare(FormatUtils.bitmap2Bytes(resource));
                            } catch (Exception e) {
                                doWeChatShare(null);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            doWeChatShare(null);
                        }
                    });
        } else {
            new Thread(() -> {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), (int) thumbImgUrl);
                doWeChatShare(FormatUtils.bitmap2Bytes(bitmap));
            }).start();
        }
    }

    /**
     * 分享文件
     *
     * @param scene    int 场景 1 分享给朋友 2 分享到朋友圈
     * @param filepath String 文件路径
     */
    public void shareFile(int scene, String filepath) {

        this.scene = scene;

        WXFileObject fileObject = new WXFileObject();
        fileObject.filePath = filepath;

        // 用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(fileObject);
        msg.title = new File(filepath).getName();
        msg.description = "文件分享";
        msg.thumbData = FormatUtils.bitmap2Bytes(BitmapFactory.decodeResource(context.getResources(), APPUtils.getAppLogoResId(MyApplication.createApplication())));

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "" + System.currentTimeMillis();
        req.message = msg;
        req.scene = scene == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        // 调用api接口，发送数据到微信
        api.sendReq(req);
    }

}
