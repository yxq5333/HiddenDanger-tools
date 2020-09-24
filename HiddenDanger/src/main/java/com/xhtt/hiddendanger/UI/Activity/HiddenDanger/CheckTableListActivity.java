package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseActivity;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.CheckTableListFragment;

/**
 * 检查表列表界面
 *
 * @author HG
 */

public class CheckTableListActivity extends BaseActivity {

    private Company parentData;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_check_table_list;
    }

    @Override
    public void initParamData() {
        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_check_table_list);

        CheckTableListFragment checkTableListFragment = new CheckTableListFragment();
        checkTableListFragment.setFragmentArguments(
                "1",
                new String[]{ParamKey.ParentData.getValue()},
                new Object[]{parentData}
        );

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_content, checkTableListFragment);
        ft.commitAllowingStateLoss();

        return null;
    }

    @Override
    public void setListener() {

    }

}
