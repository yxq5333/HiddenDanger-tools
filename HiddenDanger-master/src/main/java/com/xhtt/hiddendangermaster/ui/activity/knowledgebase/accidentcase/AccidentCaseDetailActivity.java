package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.accidentcase;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.common.FileDetail;
import com.xhtt.hiddendangermaster.constant.SystemConfig;

/**
 * 详情界面
 *
 * @author HG
 */
@Deprecated
public class AccidentCaseDetailActivity extends BaseActivity {

    private TextView title;
    private View timeAuthor;
    private TextView time;
    private TextView author;
    private View contentView;
    private WebView content;

    private FileDetail fileDetail;

    @Override
    public int bindLayout() {
        return R.layout.activity_accident_case_detail;
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
            content = findViewById(R.id.webView);

            content.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            content.getSettings().setDefaultTextEncodingName("UTF-8");

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
//                content.loadData(getHtmlData(fileDetail.getContent()), "text/html; charset=UTF-8", null);
                content.loadDataWithBaseURL(null, getHtmlData(fileDetail.getContent()), "text/html", "utf-8", null);
//                content.loadDataWithBaseURL(null, var2.toString(), "text/html", "utf-8", null);
            }
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {
        new Handler().postDelayed(() -> baseUI.setStatus(HGStatusLayout.Status.Default), SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    private String getHtmlData(String bodyHTML) {

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><body>");
        html.append("<head>");
        html.append("<title>详情</title>");
        html.append("<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" />");
        html.append("<style>img{max-width: 100%; width:auto; height:auto;}</style>");
        html.append("</head>");

        html.append("<div>");
        html.append(bodyHTML);
        html.append("</div>");

        html.append("</body></html>");

        return html.toString();
    }

}
