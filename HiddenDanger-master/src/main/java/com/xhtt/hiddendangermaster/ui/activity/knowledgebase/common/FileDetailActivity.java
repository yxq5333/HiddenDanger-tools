package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.bean.file.AppFile;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.ui.base.click.OnRecyclerViewItemClickOldListener;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.hg.hollowgoods.util.StringUtils;
import com.hg.hollowgoods.util.SystemAppUtils;
import com.hg.hollowgoods.util.anim.recyclerview.adapters.ScaleInAnimationAdapter;
import com.hg.hollowgoods.util.anim.recyclerview.animators.LandingAnimator;
import com.hg.hollowgoods.util.file.FileUtils2;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.service.JavaScriptInterface;
import com.xhtt.hiddendangermaster.adapter.knowledgebase.common.FileDetailAdapter;
import com.xhtt.hiddendangermaster.bean.knowledgebase.common.FileDetail;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.plugin.PlayVideoActivity;
import com.xhtt.hiddendangermaster.util.uploadfile.UploadFileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件详情界面
 *
 * @author HG
 */

public class FileDetailActivity extends BaseMVPActivity<FileDetailPresenter> implements FileDetailContract.View {

    private TextView title;
    private View timeAuthor;
    private TextView time;
    private TextView author;
    private View contentView;
    private WebView content;
    private View filesView;
    private RecyclerView files;
    private View memoView;
    private TextView memo;

    private FileDetail fileDetail;
    private FileDetailAdapter fileAdapter;
    private ArrayList<AppFile> fileData = new ArrayList<>();

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_file_detail;
    }

    @Override
    public void initParamData() {

        fileDetail = baseUI.getParam(HGParamKey.AppFiles, new FileDetail());

        if (TextUtils.isEmpty(fileDetail.getTitle())) {
            fileDetail.setTitle("无标题");
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, fileDetail.getActivityTitle());
        baseUI.setStatus(HGStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            title = findViewById(R.id.tv_title);
            timeAuthor = findViewById(R.id.ll_timeAuthor);
            time = findViewById(R.id.tv_time);
            author = findViewById(R.id.tv_author);
            contentView = findViewById(R.id.ll_content);
            content = findViewById(R.id.content);
            filesView = findViewById(R.id.ll_files);
            files = findViewById(R.id.rv_files);
            memoView = findViewById(R.id.ll_memo);
            memo = findViewById(R.id.tv_memo);

            // 标题
            title.setText(fileDetail.getTitle());
            // 发布时间和作者
            if (TextUtils.isEmpty(fileDetail.getReleaseTime()) && TextUtils.isEmpty(fileDetail.getAuthor())) {
                timeAuthor.setVisibility(View.GONE);
            } else {
                // 发布时间
                time.setText(fileDetail.getReleaseTime());

                // 作者
                if (TextUtils.isEmpty(fileDetail.getAuthor())) {
                    author.setText("");
                } else {
                    author.setText(fileDetail.getAuthor());
                }
            }
            // 正文
            if (TextUtils.isEmpty(fileDetail.getContent())) {
                contentView.setVisibility(View.GONE);
            } else {
                content.getSettings().setJavaScriptEnabled(true); ///------- 设置javascript 可用
                //        webView.getSettings().setLoadWithOverviewMode(true);
                //        webView.getSettings().setUseWideViewPort(true);
                // 设置允许JS弹窗
                content.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                content.getSettings().setDomStorageEnabled(true);  //很关键！！！！

                JavaScriptInterface JSInterface = new JavaScriptInterface(this); ////------
                content.addJavascriptInterface(JSInterface, "android"); // 设置js接口  第一个参数事件接口实例，第二个是实例在js中的别名，这个在js中会用到

                content.loadDataWithBaseURL(null, getHtmlData(fileDetail.getContent()), "text/html", "utf-8", null);
//                content.loadUrl("file:///android_asset/Test.html");

                // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
                content.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                        view.loadUrl(url);
                        return true;
                    }
                });

                // 由于设置了弹窗检验调用结果,所以需要支持js对话框
                // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
                // 通过设置WebChromeClient对象处理JavaScript的对话框
                // 设置响应js 的Alert()函数
                content.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                        AlertDialog.Builder b = new AlertDialog.Builder(baseUI.getBaseContext());
                        b.setTitle("Alert");
                        b.setMessage(message);
                        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                        b.setCancelable(false);
                        b.create().show();

                        return true;
                    }
                });
            }
            // 附件
            if (fileDetail.getWebFiles() == null || fileDetail.getWebFiles().size() == 0) {
                filesView.setVisibility(View.GONE);
            } else {
                files.setHasFixedSize(true);
                files.setItemAnimator(new LandingAnimator());
                files.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));
                files.setNestedScrollingEnabled(false);

                List<AppFile> temp = UploadFileUtils.webFiles2AppFiles(fileDetail.getWebFiles());
                if (temp != null) {
                    fileData.addAll(temp);
                }

                fileAdapter = new FileDetailAdapter(baseUI.getBaseContext(), R.layout.item_file_detail, fileData);
                files.setAdapter(new ScaleInAnimationAdapter(fileAdapter));
            }
            // 备注
            if (TextUtils.isEmpty(fileDetail.getMemo())) {
                memoView.setVisibility(View.GONE);
            } else {
                memo.setText(fileDetail.getMemo());
            }
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            if (fileAdapter != null) {
                fileAdapter.setOnItemClickListener(new OnRecyclerViewItemClickOldListener(false) {
                    @Override
                    public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                        new SystemAppUtils().readFile(baseUI.getBaseContext(),
                                fileData.get(position).getUrl(),
                                fileData.get(position).getOldName()
                        );
                    }
                });
            }

            baseUI.setStatus(HGStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public FileDetailPresenter createPresenter() {
        return new FileDetailPresenter();
    }

    @Override
    public void onEventUI(HGEvent event) {

        String title;
        String url;

        if (event.getEventActionCode() == EventActionCode.OPEN_WEB_FILE) {
            title = event.getObj(HGParamKey.Title, "");
            url = event.getObj(ParamKey.URL, "");

            if (FileUtils2.isImageFile(url)) {
                new SystemAppUtils().previewPhotos(baseUI.getBaseContext(), url);
            } else if (FileUtils2.isOfficeFile(url)) {
                new SystemAppUtils().readFile(baseUI.getBaseContext(), url, title);
            }
        } else if (event.getEventActionCode() == EventActionCode.PLAY_VIDEO) {
            title = event.getObj(HGParamKey.Title, "");
            url = event.getObj(ParamKey.URL, "");

            baseUI.startMyActivity(PlayVideoActivity.class,
                    new Enum[]{HGParamKey.Title, ParamKey.URL},
                    new Object[]{title, url}
            );
        }
    }

    private String getHtmlData(String bodyHTML) {

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><body>");
        html.append("<head>");
        html.append("<title>详情</title>");
        html.append("<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" />");
        html.append("<style>");
        html.append("img{max-width: 100%; width:auto; height:auto;}");
        html.append("video{max-width: 100%; width:auto; height:auto;}");
        html.append("</style>");
        html.append("</head>");

        html.append("<script>");

        html.append("function javaOpenImage(img){");
        html.append("var url = img.src;");
        html.append("android.openFile(\"\", url);");
        html.append("}");

        html.append("function javaOpenFile(file){");
        html.append("var title = file.title;");
        html.append("var url = file.id;");
        html.append("android.openFile(title, url);");
        html.append("}");

        html.append("function javaOpenVideo(video){");
        html.append("var title = \"视频播放\";");
        html.append("var url = video.id;");
        html.append("android.playVideo(title, url);");
        html.append("}");

        html.append("</script>");

        html.append("<div>");
        html.append(bodyHTML);
        html.append("</div>");

        html.append("</body></html>");

        String result = html.toString();
        result = result.replaceAll("src=\"/proxyApi/file/show/", "onclick=\"javaOpenImage(this)\" src=\"" + IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.ShowFile.getUrl()));
        result = result.replaceAll("src=\"/proxyApi/file/down/", "onclick=\"javaOpenImage(this)\" src=\"" + IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.ShowFile.getUrl()));
        result = result.replaceAll("src=\"/static/UEditor/dialogs/attachment/fileTypeImages/", "src=\"" + IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.ShowFile.getUrl()));
        result = result.replaceAll("href=\"/proxyApi/file/show/", "onclick=\"javaOpenFile(this)\" id=\"" + IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.DownloadFile.getUrl()));
        result = result.replaceAll("href=\"/proxyApi/file/down/", "onclick=\"javaOpenFile(this)\" id=\"" + IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.DownloadFile.getUrl()));

        List<String> str = StringUtils.getStringArray(result, "<");
        StringBuilder result2 = new StringBuilder();
        for (String t : str) {
            if (!TextUtils.isEmpty(t)) {
                if (!t.startsWith("<")) {
                    result2.append("<");
                }
                if (t.startsWith("video")) {
                    String temp = t;
                    temp = temp.replace("video", "img");
                    temp = temp.replace("src=", "src=\"" + IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.ShowFile.getUrl()) + "dd.jpg\"" + " id=");
                    temp = temp.replace("controls=\"\"", "");
                    temp = temp.replace("onclick=\"javaOpenImage(this)\"", "onclick=\"javaOpenVideo(this)\"");
                    result2.append(temp);
                } else if (t.startsWith("source")) {
                    result2.append(t.replace("onclick=\"javaOpenImage(this)\"", ""));
                } else {
                    result2.append(t);
                }
            }
        }

        return result2.toString();
    }

    @Override
    public void onBackPressed() {
        if (content.canGoBack()) {
            content.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
