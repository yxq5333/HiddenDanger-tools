package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.statistics;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hg.zero.adapter.viewpager.ZFragmentViewPagerAdapter;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.TabEntity;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPFragment;
import com.xhtt.hiddendangermaster.view.tablayout.CommonTabLayout;
import com.xhtt.hiddendangermaster.view.tablayout.listener.CustomTabEntity;
import com.xhtt.hiddendangermaster.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * 统计报表界面
 *
 * @author HG
 */

public class StatisticsFragment extends HDBaseMVPFragment<StatisticsPresenter> implements StatisticsContract.View {

    private CommonTabLayout tabBar;
    private ViewPager viewPager;
    private View toTop;

    private ArrayList<CustomTabEntity> tabBarData = new ArrayList<CustomTabEntity>() {
        {
            add(new TabEntity("检查企业", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
            add(new TabEntity("隐患排查", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
        }
    };
    private ZFragmentViewPagerAdapter adapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int nowPosition = 0;
    private ServiceCompanyFragment fragment1;
    private HiddenStatisticsFragment fragment2;

    @Override
    public int bindLayout() {
        return R.layout.fragement_statistics;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(R.string.title_fragment_statistics);
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            tabBar = baseUI.findViewById(R.id.tabBar);
            viewPager = baseUI.findViewById(R.id.viewPager);
            toTop = baseUI.findViewById(R.id.fab_toTop);

            tabBar.setTabData(tabBarData);

            fragment1 = new ServiceCompanyFragment();
            fragments.add(fragment1);

            fragment2 = new HiddenStatisticsFragment();
            fragments.add(fragment2);

            adapter = new ZFragmentViewPagerAdapter(getChildFragmentManager(), fragments);
            viewPager.setOffscreenPageLimit(fragments.size());
            viewPager.setAdapter(adapter);
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

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

            toTop.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {

                    if (nowPosition == 0) {
                        fragment1.toTop();
                    } else {
                        fragment2.toTop();
                    }
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public StatisticsPresenter createPresenter() {
        return new StatisticsPresenter();
    }

}
