package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hg.zero.adapter.viewpager.ZFragmentViewPagerAdapter;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.bean.knowledgebase.TabEntity;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerListContract;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerListFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerListPresenter;
import com.xhtt.hiddendangermaster.view.tablayout.CommonTabLayout;
import com.xhtt.hiddendangermaster.view.tablayout.listener.CustomTabEntity;
import com.xhtt.hiddendangermaster.view.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 隐患排查列表界面
 *
 * @author HG
 */

public class HiddenDangerListActivity extends HDBaseMVPActivity<HiddenDangerListPresenter> implements HiddenDangerListContract.View {

    private final int DIALOG_CODE_ASK_SUBMIT = 1000;
    private final int DIALOG_CODE_SUBMIT = 1001;

    private CommonTabLayout tabBar;
    private ViewPager viewPager;
    private Button add;
    private Button checkTable;

    private ArrayList<CustomTabEntity> tabBarData = new ArrayList<CustomTabEntity>() {
        {
            add(new TabEntity("未整改", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
            add(new TabEntity("已整改", R.drawable.z_ic_android_green_24dp, R.drawable.z_ic_android_green_24dp));
        }
    };
    private ZFragmentViewPagerAdapter adapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int nowPosition = 0;
    private HiddenDangerListFragment fragment1;
    private HiddenDangerListFragment fragment2;
    private Company parentData;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger_list;
    }

    @Override
    public void initParamData() {

        super.initParamData();
        parentData = baseUI.getParam(ParamKey.ParentData, null);

        if (parentData == null) {
            parentData = new Company();
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), parentData.getCompanyName());
        baseUI.setCommonRightTitleText("下一步");
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            tabBar = findViewById(R.id.tabBar);
            viewPager = findViewById(R.id.viewPager);
            add = findViewById(R.id.btn_add);
            checkTable = findViewById(R.id.btn_checkTable);

            tabBar.setTabData(tabBarData);

            fragment1 = new HiddenDangerListFragment();
            fragment1.setFragmentArguments(
                    "1",
                    new Enum[]{ParamKey.Status, ParamKey.ParentData},
                    new Object[]{HiddenDanger.STATUS_UNCHANGED, parentData}
            );
            fragments.add(fragment1);

            fragment2 = new HiddenDangerListFragment();
            fragment2.setFragmentArguments(
                    "2",
                    new Enum[]{ParamKey.Status, ParamKey.ParentData},
                    new Object[]{HiddenDanger.STATUS_CHANGED, parentData}
            );
            fragments.add(fragment2);

            adapter = new ZFragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
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

            add.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new Enum[]{ParamKey.WorkType, ParamKey.GrandData},
                            new Object[]{WorkType.AddFreeTake, parentData}
                    );
                }
            });

            checkTable.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(CheckTableListActivity.class,
                            new Enum[]{ParamKey.ParentData},
                            new Object[]{parentData}
                    );
                }
            });

            baseUI.baseDialog.addOnDialogClickListener((code, result, bundle) -> {

                if (result) {
                    switch (code) {
                        case DIALOG_CODE_ASK_SUBMIT:
                            baseUI.baseDialog.showProgressDialog(new ZDialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                                    .setContent("提交中，请稍候……")
                            );
                            mPresenter.submitService(parentData.getServiceId());
                            break;
                    }
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public HiddenDangerListPresenter createPresenter() {
        return new HiddenDangerListPresenter();
    }

    @Override
    public void onRightTitleClick(View view, int id) {
        baseUI.startMyActivity(ServiceSubmitActivity.class,
                new Enum[]{ParamKey.ParentData},
                new Object[]{parentData}
        );
//        baseUI.baseDialog.showAlertDialog(R.string.z_tips_best, "确定要提交吗？", DIALOG_CODE_ASK_SUBMIT);
    }

    @Override
    public void onEventUI(ZEvent event) {
        if (event.getEventActionCode() == EventActionCode.SERVICE_SUBMIT) {
            finishMyActivity();
        }
    }

    @Override
    public void submitServiceSuccess(long newServiceId) {

        Zt.success("提交成功");

        ZEvent event = new ZEvent(EventActionCode.SERVICE_SUBMIT);
        event.addObj(ParamKey.BackData, newServiceId);
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

    @Override
    public void submitServiceError() {

    }

    @Override
    public void submitServiceFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_SUBMIT);
    }
}
