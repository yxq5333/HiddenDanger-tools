package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
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
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CompanyDetailContract;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CompanyDetailFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CompanyDetailPresenter;

/**
 * 企业基本信息界面
 *
 * @author HG
 */

public class CompanyDetailActivity extends BaseMVPActivity<CompanyDetailPresenter> implements CompanyDetailContract.View {

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

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, title);
        baseUI.setStatus(HGStatusLayout.Status.Loading);

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

            submit.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    companyDetailFragment.submitData();
                }
            });

            baseUI.setStatus(HGStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Nullable
    @Override
    public CompanyDetailPresenter createPresenter() {
        return new CompanyDetailPresenter();
    }

}
