package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.zero.config.ZCommonResource;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.ui.base.HDBaseActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CheckTableListFragment;

/**
 * 检查表列表界面
 *
 * @author HG
 */

public class CheckTableListActivity extends HDBaseActivity {

    private Company parentData;

    @Override
    public int bindLayout() {
        return R.layout.activity_check_table_list;
    }

    @Override
    public void initParamData() {
        super.initParamData();
        parentData = baseUI.getParam(ParamKey.ParentData, null);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_check_table_list);

        CheckTableListFragment checkTableListFragment = new CheckTableListFragment();
        checkTableListFragment.setFragmentArguments(
                "1",
                new Enum[]{ParamKey.ParentData},
                new Object[]{parentData}
        );

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_content, checkTableListFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void setListener() {

    }

}
