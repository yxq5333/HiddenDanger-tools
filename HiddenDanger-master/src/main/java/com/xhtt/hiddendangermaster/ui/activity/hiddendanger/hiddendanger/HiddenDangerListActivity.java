package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hg.hollowgoods.adapter.viewpager.FragmentViewPagerAdapter;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.hg.widget.tablayout.CommonTabLayout;
import com.hg.widget.tablayout.listener.CustomTabEntity;
import com.hg.widget.tablayout.listener.OnTabSelectListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.bean.knowledgebase.TabEntity;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerListContract;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerListFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerListPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 隐患排查列表界面
 *
 * @author HG
 */

public class HiddenDangerListActivity extends BaseMVPActivity<HiddenDangerListPresenter> implements HiddenDangerListContract.View {

    private final int DIALOG_CODE_ASK_SUBMIT = 1000;
    private final int DIALOG_CODE_SUBMIT = 1001;

    private CommonTabLayout tabBar;
    private ViewPager viewPager;
    private Button add;
    private Button checkTable;

    private ArrayList<CustomTabEntity> tabBarData = new ArrayList<CustomTabEntity>() {
        {
            add(new TabEntity("未整改", R.drawable.ic_android_green_24dp, R.drawable.ic_android_green_24dp));
            add(new TabEntity("已整改", R.drawable.ic_android_green_24dp, R.drawable.ic_android_green_24dp));
        }
    };
    private FragmentViewPagerAdapter adapter;
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

        parentData = baseUI.getParam(ParamKey.ParentData, null);

        if (parentData == null) {
            parentData = new Company();
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, parentData.getCompanyName());
        baseUI.setCommonRightTitleText("下一步");
        baseUI.setStatus(HGStatusLayout.Status.Loading);

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

            adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
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

            add.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new Enum[]{ParamKey.WorkType, ParamKey.GrandData},
                            new Object[]{WorkType.AddFreeTake, parentData}
                    );
                }
            });

            checkTable.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(CheckTableListActivity.class,
                            new Enum[]{ParamKey.ParentData},
                            new Object[]{parentData}
                    );
                }
            });

            baseUI.baseDialog.setOnDialogClickListener((code, result, bundle) -> {

                if (result) {
                    switch (code) {
                        case DIALOG_CODE_ASK_SUBMIT:
                            baseUI.baseDialog.showProgressDialog(new DialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                                    .setText("提交中，请稍候……")
                            );
                            mPresenter.submitService(parentData.getServiceId());
                            break;
                    }
                }
            });

            baseUI.setStatus(HGStatusLayout.Status.Default);
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
//        baseUI.baseDialog.showAlertDialog(R.string.tips_best, "确定要提交吗？", DIALOG_CODE_ASK_SUBMIT);
    }

    @Override
    public void onEventUI(HGEvent event) {
        if (event.getEventActionCode() == EventActionCode.SERVICE_SUBMIT) {
            finishMyActivity();
        }
    }

    @Override
    public void submitServiceSuccess(long newServiceId) {

        t.success("提交成功");

        HGEvent event = new HGEvent(EventActionCode.SERVICE_SUBMIT);
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
