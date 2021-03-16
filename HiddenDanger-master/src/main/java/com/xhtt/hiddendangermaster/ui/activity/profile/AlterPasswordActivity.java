package com.xhtt.hiddendangermaster.ui.activity.profile;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hg.zero.config.ZCommonResource;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.util.ZViewUtils;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;
import com.xhtt.hiddendangermaster.util.LoginUtils;

/**
 * 修改密码界面
 *
 * @author HG
 */

public class AlterPasswordActivity extends HDBaseMVPActivity<AlterPasswordPresenter> implements AlterPasswordContract.View {

    private final int DIALOG_CODE_CHANGE_PASSWORD = 1000;

    private EditText oldPassword;
    private EditText newPassword1;
    private EditText newPassword2;
    private ImageView watchPassword1;
    private ImageView watchPassword2;
    private Button sure;

    private boolean canInput1Watch = false;
    private boolean canInput2Watch = false;

    @Override
    public int bindLayout() {
        return R.layout.activity_alter_password;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_alter_password);
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {
            oldPassword = findViewById(R.id.et_oldPassword);
            newPassword1 = findViewById(R.id.et_newPassword1);
            newPassword2 = findViewById(R.id.et_newPassword2);
            watchPassword1 = findViewById(R.id.iv_watch1);
            watchPassword2 = findViewById(R.id.iv_watch2);
            sure = findViewById(R.id.btn_sure);
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            watchPassword1.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    canInput1Watch = !canInput1Watch;
                    setInputWatch(newPassword1, watchPassword1, canInput1Watch);
                }
            });

            watchPassword2.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    canInput2Watch = !canInput2Watch;
                    setInputWatch(newPassword2, watchPassword2, canInput2Watch);
                }
            });

            sure.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    mPresenter.changePassword(
                            oldPassword.getText().toString(),
                            newPassword1.getText().toString(),
                            newPassword2.getText().toString()
                    );
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public AlterPasswordPresenter createPresenter() {
        return new AlterPasswordPresenter();
    }

    private void setInputWatch(EditText input, ImageView watch, boolean status) {

        if (status) {
            input.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            watch.setImageResource(R.drawable.ic_cant_watch);
        } else {
            input.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            watch.setImageResource(R.drawable.ic_can_watch);
        }

        ZViewUtils.setEditTextCursorLocation(input);
    }

    @Override
    public void changePasswordStart() {
        baseUI.baseDialog.showProgressDialog(
                new ZDialogConfig.ProgressConfig(DIALOG_CODE_CHANGE_PASSWORD)
                        .setContent("修改中，请稍候……")
        );
    }

    @Override
    public void changePasswordSuccess() {

        Zt.success("修改成功");

        new Handler().postDelayed(() -> {
            LoginUtils.autoExitApp(this);
        }, 500);
    }

    @Override
    public void changePasswordError() {

    }

    @Override
    public void changePasswordFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_CHANGE_PASSWORD);
    }
}
