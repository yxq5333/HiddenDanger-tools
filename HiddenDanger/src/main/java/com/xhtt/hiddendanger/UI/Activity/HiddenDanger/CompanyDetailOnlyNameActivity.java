package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
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
import com.xhtt.hiddendanger.Bean.HiddenDanger.CompanyOnlyName;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.CompanyDetailContract;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.CompanyDetailOnlyNameFragment;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.CompanyDetailPresenter;

/**
 * 企业基本信息界面
 *
 * @author HG
 */

public class CompanyDetailOnlyNameActivity extends BaseMVPActivity<CompanyDetailPresenter> implements CompanyDetailContract.View {

    private Button submit;

    private CompanyOnlyName parentData;
    private WorkType workType;
    private Class<?> fromClass;
    private CompanyDetailOnlyNameFragment companyDetailFragment;
    private String title = "企业基本信息";

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_company_detail;
    }

    @Override
    public void initParamData() {

        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);
        workType = baseUI.getParam(ParamKey.WorkType.getValue(), WorkType.Detail);
        fromClass = baseUI.getParam(ParamKey.FromClass.getValue(), null);

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

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, title);
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        new Handler().postDelayed(() -> {

            submit = findViewById(R.id.btn_submit);

            companyDetailFragment = new CompanyDetailOnlyNameFragment();
            companyDetailFragment.setFragmentArguments(
                    "1",
                    new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.FromClass.getValue()},
                    new Object[]{parentData, workType, fromClass}
            );

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fl_content, companyDetailFragment);
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
                    companyDetailFragment.submitData();
                }
            });

            baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Nullable
    @Override
    public CompanyDetailPresenter createPresenter() {
        return new CompanyDetailPresenter();
    }

}
