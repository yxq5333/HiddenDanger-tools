package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.zero.config.ZCommonResource;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CompanyDetailContract;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CompanyDetailFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CompanyDetailPresenter;

/**
 * 企业基本信息界面
 *
 * @author HG
 */

public class CompanyDetailActivity extends HDBaseMVPActivity<CompanyDetailPresenter> implements CompanyDetailContract.View {

    private Button submit;

    private Company parentData;
    private WorkType workType;
    private Class<?> fromClass;
    private CompanyDetailFragment companyDetailFragment;
    private String title = "企业基本信息";

    @Override
    public int bindLayout() {
        return R.layout.activity_company_detail;
    }

    @Override
    public void initParamData() {

        super.initParamData();
        parentData = baseUI.getParam(ParamKey.ParentData, null);
        workType = baseUI.getParam(ParamKey.WorkType, WorkType.Detail);
        fromClass = baseUI.getParam(ParamKey.FromClass, null);

        if (workType != null) {
            switch (workType) {
                case Add:
                    title = "添加企业基本信息";
                    break;
                case Edit:
                    title = "编辑企业基本信息";
                    break;
                case Selector:
                    title = "选择企业";
                    break;
                default:
                    title = "企业基本信息";
                    break;
            }
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), title);
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            submit = findViewById(R.id.btn_submit);

            companyDetailFragment = new CompanyDetailFragment();
            companyDetailFragment.setFragmentArguments(
                    "1",
                    new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.FromClass},
                    new Object[]{parentData, workType, fromClass}
            );

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fl_content, companyDetailFragment);
            ft.commitAllowingStateLoss();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            submit.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    companyDetailFragment.submitData();
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Nullable
    @Override
    public CompanyDetailPresenter createPresenter() {
        return new CompanyDetailPresenter();
    }

}
