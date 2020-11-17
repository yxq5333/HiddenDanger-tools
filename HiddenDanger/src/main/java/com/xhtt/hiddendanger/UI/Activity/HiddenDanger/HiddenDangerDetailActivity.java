package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.hollowgoods.Util.BeanUtils;
import com.hg.hollowgoods.Util.DensityUtils;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerDetailContract;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerDetailFragment;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerDetailPresenter;

/**
 * 隐患排查界面
 *
 * @author HG
 */

public class HiddenDangerDetailActivity extends BaseMVPActivity<HiddenDangerDetailPresenter> implements HiddenDangerDetailContract.View {

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
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger_detail;
    }

    @Override
    public void initParamData() {

        submitUnchecked = findViewById(R.id.btn_submitUnchecked);
        submitChecked = findViewById(R.id.btn_submitChecked);
        submit = findViewById(R.id.btn_submit);
        submitAndRestart = findViewById(R.id.btn_submitAndRestart);

        workType = baseUI.getParam(ParamKey.WorkType.getValue(), WorkType.Detail);
        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), new HiddenDanger());
        Company company = baseUI.getParam(ParamKey.GrandData.getValue(), new Company());
        boolean hiddenDangerAddWithOutContinue = baseUI.getParam(ParamKey.HiddenDangerAddWithOutContinue.getValue(), false);

        lastServiceId = company.getServiceId();

        grandData = BeanUtils.copy(company, Company.class);

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
                    llp.setMarginEnd(DensityUtils.dp2px(this, 16));
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
                llp.setMarginEnd(DensityUtils.dp2px(this, 16));
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

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, title);
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        new Handler().postDelayed(() -> {

            hiddenDangerDetailFragment = new HiddenDangerDetailFragment();
            hiddenDangerDetailFragment.setFragmentArguments(
                    "1",
                    new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue(), ParamKey.LastServiceId.getValue()},
                    new Object[]{parentData, workType, grandData, lastServiceId}
            );

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fl_content, hiddenDangerDetailFragment);
            ft.commitAllowingStateLoss();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);

        return null;
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            submitChecked.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    hiddenDangerDetailFragment.submitFile(HiddenDanger.STATUS_CHANGED);
                }
            });

            submitUnchecked.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    hiddenDangerDetailFragment.submitFile(HiddenDanger.STATUS_UNCHANGED);
                }
            });

            submit.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    action = ACTION_SUBMIT_ONLY;
                    hiddenDangerDetailFragment.submitFile(null);
                }
            });

            submitAndRestart.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    action = ACTION_SUBMIT_AND_RESTART;
                    hiddenDangerDetailFragment.submitFile(null);
                }
            });

            baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
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
