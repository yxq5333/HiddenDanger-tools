package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.CompanyDetailFragment;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerDetailFragment;

/**
 * 随拍随记界面
 *
 * @author HG
 */

public class FreeTakeActivity extends BaseMVPActivity<FreeTakePresenter> implements FreeTakeContract.View {

    public static final int ACTION_SUBMIT_ONLY = 1;
    public static final int ACTION_SUBMIT_AND_RESTART = 2;

    private Button submit;
    private Button submitAndRestart;

    public int action;

    private CompanyDetailFragment companyDetailFragment;
    private HiddenDangerDetailFragment hiddenDangerDetailFragment;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_free_take;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_free_take);
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        new Handler().postDelayed(() -> {

            submit = findViewById(R.id.btn_submit);
            submitAndRestart = findViewById(R.id.btn_submitAndRestart);

            Company company = new Company();
            company.setShowOther(false);
            companyDetailFragment = new CompanyDetailFragment();
            companyDetailFragment.setFragmentArguments(
                    "1",
                    new String[]{ParamKey.WorkType.getValue(), ParamKey.ParentData.getValue(), ParamKey.IsOnlySelector.getValue()},
                    new Object[]{WorkType.AddFreeTake, company, false}
            );
            companyDetailFragment.setOnFragmentWorkListener(() -> companyDetailFragment.getRefreshLayout().getRecyclerView().setNestedScrollingEnabled(false));

            hiddenDangerDetailFragment = new HiddenDangerDetailFragment();
            hiddenDangerDetailFragment.setFragmentArguments(
                    "2",
                    new String[]{ParamKey.WorkType.getValue()},
                    new Object[]{WorkType.Add}
            );
            hiddenDangerDetailFragment.setOnFragmentWorkListener(() -> {
                hiddenDangerDetailFragment.getHiddenDangerRefreshLayout().getRecyclerView().setNestedScrollingEnabled(false);
                hiddenDangerDetailFragment.getChangeRefreshLayout().getRecyclerView().setNestedScrollingEnabled(false);
            });

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fl_content1, companyDetailFragment);
            ft.add(R.id.fl_content2, hiddenDangerDetailFragment);
            ft.commitAllowingStateLoss();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);

        return null;
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            submit.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    if (companyDetailFragment.checkNotEmptyItems()) {
                        action = ACTION_SUBMIT_ONLY;

                        Company company = companyDetailFragment.getCompanyData();
                        hiddenDangerDetailFragment.submitFile(
                                company.getId(),
                                company.getCompanyName(),
                                company.getServiceId()
                        );
                    }
                }
            });

            submitAndRestart.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    if (companyDetailFragment.checkNotEmptyItems()) {
                        action = ACTION_SUBMIT_AND_RESTART;

                        Company company = companyDetailFragment.getCompanyData();
                        hiddenDangerDetailFragment.submitFile(
                                company.getId(),
                                company.getCompanyName(),
                                company.getServiceId()
                        );
                    }
                }
            });

            baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public FreeTakePresenter createPresenter() {
        return new FreeTakePresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        hiddenDangerDetailFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
