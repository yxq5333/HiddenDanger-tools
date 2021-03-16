package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hg.zero.adapter.viewpager.ZFragmentViewPagerAdapter;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.listener.ZOnViewClickListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Record;
import com.xhtt.hiddendangermaster.bean.knowledgebase.TabEntity;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CheckTableListFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerOnceListChildFragment;
import com.xhtt.hiddendangermaster.view.tablayout.CommonTabLayout;
import com.xhtt.hiddendangermaster.view.tablayout.listener.CustomTabEntity;
import com.xhtt.hiddendangermaster.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * 隐患排查单次服务列表界面
 *
 * @author HG
 */

public class HiddenDangerOnceListActivity extends HDBaseMVPActivity<HiddenDangerOnceListPresenter> implements HiddenDangerOnceListContract.View {

    private CommonTabLayout tabBar;
    private ViewPager viewPager;
    private Button add;

    private ArrayList<CustomTabEntity> tabBarData = new ArrayList<CustomTabEntity>() {
        {
            add(new TabEntity("隐患列表", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
            add(new TabEntity("检查记录表", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
        }
    };
    private ZFragmentViewPagerAdapter adapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int nowPosition = 0;
    private HiddenDangerOnceListChildFragment fragment1;
    private CheckTableListFragment fragment2;
    private Company grandData;
    private Record parentData;

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger_once_list;
    }

    @Override
    public void initParamData() {

        super.initParamData();
        grandData = baseUI.getParam(ParamKey.GrandData, null);
        parentData = baseUI.getParam(ParamKey.ParentData, null);

        if (grandData == null) {
            grandData = new Company();
        }

        if (parentData == null) {
            parentData = new Record();
        }

        grandData.setServiceId(parentData.getServiceId());
        grandData.setTimes(parentData.getTimes());
        grandData.setHiddenDangerOnce(true);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), grandData.getCompanyName());

        tabBar = findViewById(R.id.tabBar);
        viewPager = findViewById(R.id.viewPager);
        add = findViewById(R.id.btn_add);

        tabBar.setTabData(tabBarData);

        fragment1 = new HiddenDangerOnceListChildFragment();
        fragment1.setFragmentArguments(
                "1",
                new Enum[]{ParamKey.ParentData},
                new Object[]{grandData}
        );
        fragments.add(fragment1);

        fragment2 = new CheckTableListFragment();
        fragment2.setFragmentArguments(
                "2",
                new Enum[]{ParamKey.ParentData},
                new Object[]{grandData}
        );
        fragments.add(fragment2);

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

                add.setVisibility(nowPosition == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        add.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                        new Enum[]{ParamKey.WorkType, ParamKey.GrandData},
                        new Object[]{WorkType.AddFreeTake, grandData}
                );
            }
        });
    }

    @Override
    public HiddenDangerOnceListPresenter createPresenter() {
        return new HiddenDangerOnceListPresenter();
    }

}
