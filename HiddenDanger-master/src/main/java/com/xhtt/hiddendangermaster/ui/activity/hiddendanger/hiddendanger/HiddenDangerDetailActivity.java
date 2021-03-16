package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.zero.config.ZCommonResource;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.util.ZBeanUtils;
import com.hg.zero.util.ZDensityUtils;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerDetailContract;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerDetailFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerDetailPresenter;

/**
 * 隐患排查界面
 *
 * @author HG
 */

public class HiddenDangerDetailActivity extends HDBaseMVPActivity<HiddenDangerDetailPresenter> implements HiddenDangerDetailContract.View {

    public static final int ACTION_SUBMIT_ONLY = 1;
    public static final int ACTION_SUBMIT_AND_RESTART = 2;

    private Button submitUnchecked;
    private Button submitChecked;
    private Button submit;
    private Button submitAndRestart;

    private HiddenDanger parentData;
    private WorkType workType;
    private Company grandData;
    private String title;
    private HiddenDangerDetailFragment hiddenDangerDetailFragment;
    private long lastServiceId;
    public int action = ACTION_SUBMIT_ONLY;

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger_detail;
    }

    @Override
    public void initParamData() {

        super.initParamData();

        submitUnchecked = findViewById(R.id.btn_submitUnchecked);
        submitChecked = findViewById(R.id.btn_submitChecked);
        submit = findViewById(R.id.btn_submit);
        submitAndRestart = findViewById(R.id.btn_submitAndRestart);

        workType = baseUI.getParam(ParamKey.WorkType, WorkType.Detail);
        parentData = baseUI.getParam(ParamKey.ParentData, new HiddenDanger());
        Company company = baseUI.getParam(ParamKey.GrandData, new Company());
        boolean hiddenDangerAddWithOutContinue = baseUI.getParam(ParamKey.HiddenDangerAddWithOutContinue, false);

        lastServiceId = company.getServiceId();

        grandData = ZBeanUtils.copy(company, Company.class);

        if (workType == null) {
            workType = WorkType.Detail;
        }

        switch (workType) {
            case Add:
                title = "添加隐患";
                findViewById(R.id.ll_submitView).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_changeView).setVisibility(View.GONE);
                if (hiddenDangerAddWithOutContinue) {
                    submitAndRestart.setVisibility(View.GONE);
                    LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) submit.getLayoutParams();
                    llp.setMarginEnd(ZDensityUtils.dp2px(this, 16));
                }
                break;
            case AddFreeTake:
                title = "随拍随记";
                findViewById(R.id.ll_submitView).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_changeView).setVisibility(View.GONE);
                break;
            case Edit:
                title = "编辑隐患";
                grandData.setServiceId(parentData.getServiceId());
                findViewById(R.id.ll_submitView).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_changeView).setVisibility(View.GONE);
                submitAndRestart.setVisibility(View.GONE);
                LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) submit.getLayoutParams();
                llp.setMarginEnd(ZDensityUtils.dp2px(this, 16));
                break;
            case Change:
                title = "隐患排查";
                grandData.setServiceId(parentData.getServiceId());
                submitChecked.setText("整改完成");
                findViewById(R.id.ll_submitView).setVisibility(View.GONE);
                findViewById(R.id.ll_changeView).setVisibility(View.VISIBLE);
                break;
            default:
                title = "隐患排查";
                findViewById(R.id.ll_submitView).setVisibility(View.GONE);
                findViewById(R.id.ll_changeView).setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), title);
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            hiddenDangerDetailFragment = new HiddenDangerDetailFragment();
            hiddenDangerDetailFragment.setFragmentArguments(
                    "1",
                    new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.GrandData, ParamKey.LastServiceId},
                    new Object[]{parentData, workType, grandData, lastServiceId}
            );

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fl_content, hiddenDangerDetailFragment);
            ft.commitAllowingStateLoss();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            submitChecked.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    hiddenDangerDetailFragment.submitFile(HiddenDanger.STATUS_CHANGED);
                }
            });

            submitUnchecked.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    hiddenDangerDetailFragment.submitFile(HiddenDanger.STATUS_UNCHANGED);
                }
            });

            submit.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    action = ACTION_SUBMIT_ONLY;
                    hiddenDangerDetailFragment.submitFile(null);
                }
            });

            submitAndRestart.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    action = ACTION_SUBMIT_AND_RESTART;
                    hiddenDangerDetailFragment.submitFile(null);
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public HiddenDangerDetailPresenter createPresenter() {
        return new HiddenDangerDetailPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        hiddenDangerDetailFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
