package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.hg.hollowgoods.Bean.AppFile;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.Constant.HGParamKey;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.Util.UploadFile.UploadFileUtils;
import com.xhtt.hiddendanger.View.DoodleView.DoodleView;

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
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_sign;
    }

    @Override
    public void initParamData() {

    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

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

        return this;
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

                baseUI.baseDialog.showProgressDialog("正在上传签名，请稍候……", DIALOG_CODE_SUBMIT);

                ArrayList<AppFile> appFiles = new ArrayList<>();

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
    public void onEventUI(Event event) {
        if (event.isFromMe(this.getClass().getName())) {
            if (event.getEventActionCode() == EventActionCode.UPLOAD_PHOTO) {
                boolean status = event.getObj(ParamKey.Status.getValue(), false);

                if (status) {
                    ArrayList<AppFile> appFiles = event.getObj(ParamKey.BackData.getValue(), null);
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

        Event event = new Event(EventActionCode.SIGN_BACK);
        event.addObj(HGParamKey.RequestCode.getValue(), baseUI.requestCode);
        if (appFiles != null) {
            event.addObj(ParamKey.SignData.getValue(), appFiles);
        }
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

}
