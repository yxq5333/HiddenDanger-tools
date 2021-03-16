package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.laws;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hg.zero.adapter.viewpager.ZFragmentViewPagerAdapter;
import com.hg.zero.config.ZCommonResource;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.TabEntity;
import com.xhtt.hiddendangermaster.constant.LawType;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.ui.base.HDBaseActivity;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.laws.LawsFragment;
import com.xhtt.hiddendangermaster.view.tablayout.CommonTabLayout;
import com.xhtt.hiddendangermaster.view.tablayout.listener.CustomTabEntity;
import com.xhtt.hiddendangermaster.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * 法律法规界面
 *
 * @author HG
 */

public class LawsActivity extends HDBaseActivity {

    private CommonTabLayout tabBar;
    private ViewPager viewPager;

    private ArrayList<CustomTabEntity> tabBarData = new ArrayList<CustomTabEntity>() {
        {
            add(new TabEntity("法律", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
            add(new TabEntity("法规", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
            add(new TabEntity("规章", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
            add(new TabEntity("文件", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
        }
    };
    private ZFragmentViewPagerAdapter adapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int nowPosition = 0;
    private LawsFragment legalFragment;
    private LawsFragment lawFragment;
    private LawsFragment ruleFragment;
    private LawsFragment fileFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_laws;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_laws);

        tabBar = findViewById(R.id.tabBar);
        viewPager = findViewById(R.id.viewPager);

        tabBar.setTabData(tabBarData);

        legalFragment = new LawsFragment();
        legalFragment.setFragmentArguments(
                LawType.Legal.name(),
                new Enum[]{ParamKey.LawType},
                new Object[]{LawType.Legal}
        );
        fragments.add(legalFragment);

        lawFragment = new LawsFragment();
        lawFragment.setFragmentArguments(
                LawType.Law.name(),
                new Enum[]{ParamKey.LawType},
                new Object[]{LawType.Law}
        );
        fragments.add(lawFragment);

        ruleFragment = new LawsFragment();
        ruleFragment.setFragmentArguments(
                LawType.Rule.name(),
                new Enum[]{ParamKey.LawType},
                new Object[]{LawType.Rule}
        );
        fragments.add(ruleFragment);

        fileFragment = new LawsFragment();
        fileFragment.setFragmentArguments(
                LawType.File.name(),
                new Enum[]{ParamKey.LawType},
                new Object[]{LawType.File}
        );
        fragments.add(fileFragment);

        adapter = new ZFragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setListener() {

        tabBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position, true);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                nowPosition = position;
                tabBar.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
