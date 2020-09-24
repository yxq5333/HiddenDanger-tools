package com.xhtt.hiddendanger.UI.Activity.Common;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.Constant.HGConstants;
import com.hg.hollowgoods.Constant.HGParamKey;
import com.hg.hollowgoods.Constant.HGSystemConfig;
import com.hg.hollowgoods.R;
import com.hg.hollowgoods.UI.Base.BaseActivity;
import com.hg.hollowgoods.Util.FileUtils;
import com.hg.hollowgoods.Util.StringUtils;
import com.hg.hollowgoods.Util.XUtils.DownloadListener;
import com.hg.hollowgoods.Util.XUtils.RequestParamsHelper;
import com.hg.hollowgoods.Widget.FileRead.SuperFileView;
import com.xhtt.hiddendanger.Util.XUtils2;

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

    private SuperFileView fileView;

    private String url;
    private String filepath;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_file_read;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        fileView = findViewById(R.id.fileView);

        url = getIntent().getStringExtra(HGParamKey.URL.getValue());
        String title = getIntent().getStringExtra(HGParamKey.Title.getValue());

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, title);

        if (!url.toLowerCase().startsWith("http://")) {
            filepath = url;
            baseUI.setCommonRightTitleText("下载");
        }

        fileView.setOnGetFilePathListener(superFileView -> checkFilepath());
        fileView.show();

        return null;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onRightTitleClick(View view, int id) {
        baseUI.baseDialog.showTipDialog(R.string.tips_best, "已下载至：" + filepath, HGConstants.DEFAULT_CODE);
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

        if (FileUtils.checkFileExistOnly(CACHE_PATH + getFileName())) {
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
