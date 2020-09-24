package com.xhtt.hiddendangermaster.ui.activity.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.util.IdentifyingCode;

/**
 * 注册界面
 *
 * @author HG
 */
public class RegisterActivity extends BaseMVPActivity<RegisterPresenter> implements RegisterContract.View {

    private final int DIALOG_CODE_REGISTER = 1000;

    private EditText phone;
    private EditText password;
    private EditText code;
    private ImageView refreshCode;
    private Button register;

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_register);

        phone = findViewById(R.id.et_phone);
        code = findViewById(R.id.et_code);
        refreshCode = findViewById(R.id.iv_code);
        password = findViewById(R.id.et_password);
        register = findViewById(R.id.btn_register);

        refreshCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
    }

    @Override
    public void setListener() {

        refreshCode.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                refreshCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
            }
        });

        register.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                mPresenter.doRegister(
                        phone.getText().toString(),
                        phone.getText().toString(),
                        "yhpc_" + phone.getText().toString(),
                        code.getText().toString(),
                        IdentifyingCode.getInstance().getCode(),
                        password.getText().toString()
                );
            }
        });
    }

    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void doRegisterStart() {
        baseUI.baseDialog.showProgressDialog(
                new DialogConfig.ProgressConfig(DIALOG_CODE_REGISTER)
                        .setText("注册中，请稍候……")
        );
    }

    @Override
    public void doRegisterSuccess() {
        t.success("注册成功");
        finishMyActivity();
    }

    @Override
    public void doRegisterError() {

    }

    @Override
    public void doRegisterFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_REGISTER);
    }
}
