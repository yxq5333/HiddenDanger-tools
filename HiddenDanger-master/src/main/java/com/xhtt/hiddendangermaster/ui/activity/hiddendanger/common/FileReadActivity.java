package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.constant.HGSystemConfig;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.hg.hollowgoods.util.StringUtils;
import com.hg.hollowgoods.util.file.FileUtils2;
import com.hg.hollowgoods.util.xutils.RequestParamsHelper;
import com.hg.hollowgoods.util.xutils.XUtils2;
import com.hg.hollowgoods.util.xutils.callback.base.DownloadListener;
import com.hg.hollowgoods.widget.fileread.SuperFileView;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.ui.dialog.ShareWeChatConfig;
import com.xhtt.hiddendangermaster.ui.dialog.ShareWeChatDialog;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.io.File;

/**
 * 文件查看界面
 * <p>
 * Created by Hollow Goods on 2018-10-09.
 */
public class FileReadActivity extends BaseActivity {

    private final String CACHE_PATH = HGSystemConfig.getDataCachePath();
    private final int DIALOG_CODE_SHARE = 1000;

    private SuperFileView fileView;

    private String url;
    private String filepath;

    @Override
    public int bindLayout() {
        return R.layout.activity_file_read;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        fileView = findViewById(R.id.fileView);

        url = getIntent().getStringExtra(HGParamKey.URL.toString());
        String title = getIntent().getStringExtra(HGParamKey.Title.toString());

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, title);

        if (!url.toLowerCase().startsWith("http://")) {
            filepath = url;
            baseUI.setCommonRightTitleText("分享");
        }

        fileView.setOnGetFilePathListener(superFileView -> checkFilepath());
        fileView.show();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onRightTitleClick(View view, int id) {
//        baseUI.baseDialog.showTipDialog(new DialogConfig.TipConfig(HGConstants.DEFAULT_CODE)
//                .setTitle(R.string.tips_best)
//                .setText("已下载至：" + filepath)
//        );

        ShareWeChatConfig config = new ShareWeChatConfig(DIALOG_CODE_SHARE);
        config.setFilepath(filepath);

        baseUI.baseDialog.showCustomizeDialog(config, new ShareWeChatDialog());
    }

    @Override
    public boolean haveScroll() {
        return true;
    }

    private void checkFilepath() {
        if (TextUtils.isEmpty(filepath)) {//网络地址要先下载
            downloadFile();
        } else {
            readFile();
        }
    }

    private void readFile() {
        fileView.displayFile(new File(filepath));
    }

    private void downloadFile() {

        if (FileUtils2.checkFileExistOnly(CACHE_PATH + getFileName())) {
            filepath = CACHE_PATH + getFileName();
            readFile();
        } else {
            RequestParams params = RequestParamsHelper.buildJsonBodyRequestParam(HttpMethod.GET, url, null, null);

            new XUtils2.BuilderDownloadFile().setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadSuccess(File file) {
                    filepath = file.getAbsolutePath();
                    baseUI.setCommonRightTitleText("下载");
                    readFile();
                }

                @Override
                public void onDownloadError(Throwable ex) {

                }

                @Override
                public void onDownloadLoading(long total, long current) {

                }

                @Override
                public void onDownloadFinish() {

                }

                @Override
                public void onDownloadCancel(Callback.CancelledException cex) {

                }
            }).downloadFile(params, CACHE_PATH + getFileName());
        }
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @return String
     */
    private String getFileName() {
        return StringUtils.getTimeForFilename() + ".pdf";
    }

    /***
     * 获取文件类型
     *
     * @return String
     */
    private String getFileType() {

        if (TextUtils.isEmpty(url)) {
            return "";
        }

        int index = url.lastIndexOf(".");

        if (index <= -1) {
            return "";
        }

        return url.substring(index + 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fileView != null) {
            fileView.onStopDisplay();
        }
    }

}
