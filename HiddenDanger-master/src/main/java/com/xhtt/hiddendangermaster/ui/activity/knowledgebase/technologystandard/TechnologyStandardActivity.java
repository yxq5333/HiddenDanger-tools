package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.technologystandard;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.zero.config.ZCommonResource;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.ui.base.HDBaseActivity;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.technologystandard.TechnologyStandardFragment;

/**
 * 技术标准界面
 *
 * @author HG
 */

public class TechnologyStandardActivity extends HDBaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_technology_standard;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_technology_standard);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_technologyStandard, new TechnologyStandardFragment());
        ft.commitAllowingStateLoss();
    }

    @Override
    public void setListener() {

    }

}
