package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hg.hollowgoods.adapter.viewpager.FragmentViewPagerAdapter;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.hg.widget.tablayout.CommonTabLayout;
import com.hg.widget.tablayout.listener.CustomTabEntity;
import com.hg.widget.tablayout.listener.OnTabSelectListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Record;
import com.xhtt.hiddendangermaster.bean.knowledgebase.TabEntity;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CheckTableListFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerOnceListChildFragment;

import java.util.ArrayList;

/**
 * 隐患排查单次服务列表界面
 *
 * @author HG
 */

public class HiddenDangerOnceListActivity extends BaseMVPActivity<HiddenDangerOnceListPresenter> implements HiddenDangerOnceListContract.View {

    private CommonTabLayout tabBar;
    private ViewPager viewPager;
    private Button add;

    private ArrayList<CustomTabEntity> tabBarData = new ArrayList<CustomTabEntity>() {
        {
            add(new TabEntity("隐患列表", R.drawable.ic_android_green_24dp, R.drawable.ic_android_green_24dp));
            add(new TabEntity("检查记录表", R.drawable.ic_android_green_24dp, R.drawable.ic_android_green_24dp));
        }
    };
    private FragmentViewPagerAdapter adapter;
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

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, grandData.getCompanyName());

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

        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
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

        add.setOnClickListener(new OnViewClickListener(false) {
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
