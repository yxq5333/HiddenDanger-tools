package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CompanyDetailFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerDetailFragment;

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
    public int bindLayout() {
        return R.layout.activity_free_take;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_free_take);
        baseUI.setStatus(HGStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            submit = findViewById(R.id.btn_submit);
            submitAndRestart = findViewById(R.id.btn_submitAndRestart);

            Company company = new Company();
            company.setShowOther(false);
            companyDetailFragment = new CompanyDetailFragment();
            companyDetailFragment.setFragmentArguments(
                    "1",
                    new Enum[]{ParamKey.WorkType, ParamKey.ParentData, ParamKey.IsOnlySelector},
                    new Object[]{WorkType.AddFreeTake, company, false}
            );
            companyDetailFragment.setOnFragmentWorkListener(() -> companyDetailFragment.getRefreshLayout().getRecyclerView().setNestedScrollingEnabled(false));

            hiddenDangerDetailFragment = new HiddenDangerDetailFragment();
            hiddenDangerDetailFragment.setFragmentArguments(
                    "2",
                    new Enum[]{ParamKey.WorkType},
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

            baseUI.setStatus(HGStatusLayout.Status.Default);
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
