package com.xhtt.hiddendangermaster.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.ui.activity.law.LawActivity;
import com.xhtt.hiddendangermaster.ui.activity.main.MainActivity;
import com.xhtt.hiddendangermaster.ui.activity.register.RegisterActivity;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;
import com.xhtt.hiddendangermaster.util.LoginUtils;
import com.xhtt.hiddendangermaster.util.UpdateAPPUtils;

/**
 * 主界面
 *
 * @author HG
 */

public class LoginActivity extends HDBaseMVPActivity<LoginPresenter> implements LoginContract.View {

    private final int DIALOG_CODE_LOGIN = 1000;

    private TextView register;
    private Button login;
    private EditText username;
    private EditText password;
    private View logo;
    private CheckBox flag;
    private TextView avoidLawTips;


    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);
        baseUI.hideActionBar();
        baseUI.setStatusBackgroundColor(R.color.transparent);

        register = findViewById(R.id.tv_register);
        login = findViewById(R.id.btn_login);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        logo = findViewById(R.id.logo);
        flag = findViewById(R.id.cb_flag);
        avoidLawTips = findViewById(R.id.tv_avoidLawTips);

        flag.setChecked(true);

        if (MyApplication.createApplication().getUser() != null) {
            username.setText(MyApplication.createApplication().getUser().getUsername());
            password.setText(MyApplication.createApplication().getUser().getPassword());

            doLogin();
        } else {
            new UpdateAPPUtils(baseUI.getBaseContext()).checkUpdate(false);
        }
    }

    @Override
    public void setListener() {

        flag.setOnCheckedChangeListener((buttonView, isChecked) -> login.setEnabled(isChecked));

        register.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                baseUI.startMyActivity(RegisterActivity.class);
            }
        });

        login.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                doLogin();
            }
        });

        logo.setOnLongClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewLongClick(View view, int id) {
                ZIPConfigHelper.get().showIPConfig(baseUI);
            }
        });

        avoidLawTips.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                baseUI.startMyActivity(LawActivity.class);
            }
        });
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    private void doLogin() {
        mPresenter.doLogin(
                username.getText().toString(),
                password.getText().toString()
        );
    }

    @Override
    public void doLoginStart() {
        baseUI.baseDialog.showProgressDialog(
                new ZDialogConfig.ProgressConfig(DIALOG_CODE_LOGIN)
                        .setContent("登录中，请稍候……")
        );
    }

    @Override
    public void doLoginSuccess(String token) {

        Zt.success("登录成功");

        MyApplication.createApplication().setToken(token);

        User user = new User();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        LoginUtils.updateUser(user);

        baseUI.startMyActivity(MainActivity.class);
        finish();
    }

    @Override
    public void doLoginError() {

    }

    @Override
    public void doLoginFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_LOGIN);
    }

}
