package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hg.hollowgoods.Adapter.ViewPagerAdapter.FragmentViewPagerAdapter;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.library_widget.Widget.TabLayout.CommonTabLayout;
import com.hg.library_widget.Widget.TabLayout.Listener.CustomTabEntity;
import com.hg.library_widget.Widget.TabLayout.Listener.OnTabSelectListener;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Record;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.CheckTableListFragment;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerOnceListChildFragment;
import com.xhtt.knowledgebase.Bean.TabEntity;

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
            add(new TabEntity("隐患列表", R.drawable.ic_android_green_24dp, R.drawable.ic_android_white_24dp));
            add(new TabEntity("检查记录表", R.drawable.ic_android_green_24dp, R.drawable.ic_android_white_24dp));
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
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger_once_list;
    }

    @Override
    public void initParamData() {

        grandData = baseUI.getParam(ParamKey.GrandData.getValue(), null);
        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);

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

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, grandData.getCompanyName());

        tabBar = findViewById(R.id.tabBar);
        viewPager = findViewById(R.id.viewPager);
        add = findViewById(R.id.btn_add);

        tabBar.setTabData(tabBarData);

        fragment1 = new HiddenDangerOnceListChildFragment();
        fragment1.setFragmentArguments(
                "1",
                new String[]{ParamKey.ParentData.getValue()},
                new Object[]{grandData}
        );
        fragments.add(fragment1);

        fragment2 = new CheckTableListFragment();
        fragment2.setFragmentArguments(
                "2",
                new String[]{ParamKey.ParentData.getValue()},
                new Object[]{grandData}
        );
        fragments.add(fragment2);

        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(adapter);

        return null;
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
                        new String[]{ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
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
