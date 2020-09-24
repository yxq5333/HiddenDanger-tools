package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.bean.file.AppFile;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.util.uploadfile.UploadFileUtils;
import com.xhtt.hiddendangermaster.view.doodleview.DoodleView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务单签字确认 界面
 * <p>
 * Created by YXQ on 2020-06-23
 */

public class SignActivity extends BaseMVPActivity<SignPresenter> implements SignContract.View {

    private DoodleView mDoodleView1;
    private DoodleView mDoodleView2;
    private TextView clear1;
    private TextView clear2;
    private AppCompatButton submit;

    private final int DIALOG_CODE_SUBMIT = 1000;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_sign;
    }

    @Override
    public void initParamData() {

    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, "确认签字");
        baseUI.setCommonRightTitleText("跳过");

        mDoodleView1 = findViewById(R.id.doodle_doodleView1);
        mDoodleView2 = findViewById(R.id.doodle_doodleView2);
        clear1 = findViewById(R.id.tv_clear1);
        clear2 = findViewById(R.id.tv_clear2);
        submit = findViewById(R.id.btn_submit);

        mDoodleView1.setSize(10);
        mDoodleView1.setColor("#000000");

        mDoodleView2.setSize(10);
        mDoodleView2.setColor("#000000");
    }

    @Override
    public void onRightTitleClick(View view, int id) {
        backData(null);
    }

    @Override
    public void setListener() {

        clear1.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                mDoodleView1.reset();
            }
        });

        clear2.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                mDoodleView2.reset();
            }
        });

        submit.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                baseUI.baseDialog.showProgressDialog(new DialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                        .setText("正在上传签名，请稍候……")
                );

                List<AppFile> appFiles = new ArrayList<>();

                String path1 = mDoodleView1.saveBitmap(mDoodleView1);
                AppFile appFile1 = new AppFile();
                appFile1.setFile(new File(path1));
                appFiles.add(appFile1);

                String path2 = mDoodleView2.saveBitmap(mDoodleView2);
                AppFile appFile2 = new AppFile();
                appFile2.setFile(new File(path2));
                appFiles.add(appFile2);

                new UploadFileUtils(baseUI.getBaseContext()).doUpload(SignActivity.class.getName(), appFiles);
            }
        });
    }

    @Override
    public void onEventUI(HGEvent event) {
        if (event.isFromMe(this.getClass().getName())) {
            if (event.getEventActionCode() == EventActionCode.UPLOAD_PHOTO) {
                boolean status = event.getObj(ParamKey.Status, false);

                if (status) {
                    ArrayList<AppFile> appFiles = event.getObj(ParamKey.BackData, null);
                    backData(appFiles);
                } else {
                    t.error("提交失败，请重试");
                }
            }
        }
    }

    @Override
    public SignPresenter createPresenter() {
        return new SignPresenter(this);
    }

    private void backData(List<AppFile> appFiles) {

        baseUI.baseDialog.closeDialog(DIALOG_CODE_SUBMIT);

        HGEvent event = new HGEvent(EventActionCode.SIGN_BACK);
        event.addObj(HGParamKey.RequestCode, baseUI.requestCode);
        if (appFiles != null) {
            event.addObj(ParamKey.SignData, appFiles);
        }
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

}
