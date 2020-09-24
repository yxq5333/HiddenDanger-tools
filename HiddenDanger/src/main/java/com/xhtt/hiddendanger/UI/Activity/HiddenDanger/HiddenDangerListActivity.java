package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hg.hollowgoods.Adapter.ViewPagerAdapter.FragmentViewPagerAdapter;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.library_widget.Widget.TabLayout.CommonTabLayout;
import com.hg.library_widget.Widget.TabLayout.Listener.CustomTabEntity;
import com.hg.library_widget.Widget.TabLayout.Listener.OnTabSelectListener;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerListContract;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerListFragment;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerListPresenter;
import com.xhtt.knowledgebase.Bean.TabEntity;

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
            add(new TabEntity("未整改", R.drawable.ic_android_green_24dp, R.drawable.ic_android_white_24dp));
            add(new TabEntity("已整改", R.drawable.ic_android_green_24dp, R.drawable.ic_android_white_24dp));
        }
    };
    private FragmentViewPagerAdapter adapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int nowPosition = 0;
    private HiddenDangerListFragment fragment1;
    private HiddenDangerListFragment fragment2;
    private Company parentData;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger_list;
    }

    @Override
    public void initParamData() {

        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);

        if (parentData == null) {
            parentData = new Company();
        }
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, parentData.getCompanyName());
        baseUI.setCommonRightTitleText("下一步");
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        new Handler().postDelayed(() -> {

            tabBar = findViewById(R.id.tabBar);
            viewPager = findViewById(R.id.viewPager);
            add = findViewById(R.id.btn_add);
            checkTable = findViewById(R.id.btn_checkTable);

            tabBar.setTabData(tabBarData);

            fragment1 = new HiddenDangerListFragment();
            fragment1.setFragmentArguments(
                    "1",
                    new String[]{ParamKey.Status.getValue(), ParamKey.ParentData.getValue()},
                    new Object[]{HiddenDanger.STATUS_UNCHANGED, parentData}
            );
            fragments.add(fragment1);

            fragment2 = new HiddenDangerListFragment();
            fragment2.setFragmentArguments(
                    "2",
                    new String[]{ParamKey.Status.getValue(), ParamKey.ParentData.getValue()},
                    new Object[]{HiddenDanger.STATUS_CHANGED, parentData}
            );
            fragments.add(fragment2);

            adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
            viewPager.setOffscreenPageLimit(fragments.size());
            viewPager.setAdapter(adapter);
        }, SystemConfig.DELAY_TIME_INIT_VIEW);

        return this;
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
                            new String[]{ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
                            new Object[]{WorkType.AddFreeTake, parentData}
                    );
                }
            });

            checkTable.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(CheckTableListActivity.class,
                            new String[]{ParamKey.ParentData.getValue()},
                            new Object[]{parentData}
                    );
                }
            });

            baseUI.baseDialog.setOnDialogClickListener((code, result, bundle) -> {

                if (result) {
                    switch (code) {
                        case DIALOG_CODE_ASK_SUBMIT:
                            baseUI.baseDialog.showProgressDialog("提交中，请稍候……", DIALOG_CODE_SUBMIT);
                            mPresenter.submitService(parentData.getServiceId());
                            break;
                    }
                }
            });

            baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public HiddenDangerListPresenter createPresenter() {
        return new HiddenDangerListPresenter();
    }

    @Override
    public void onRightTitleClick(View view, int id) {
        baseUI.startMyActivity(ServiceSubmitActivity.class,
                new String[]{ParamKey.ParentData.getValue()},
                new Object[]{parentData}
        );
//        baseUI.baseDialog.showAlertDialog(R.string.tips_best, "确定要提交吗？", DIALOG_CODE_ASK_SUBMIT);
    }

    @Override
    public void onEventUI(Event event) {
        switch (event.getEventActionCode()) {
            case EventActionCode.SERVICE_SUBMIT:
                finishMyActivity();
                break;
        }
    }

    @Override
    public void submitServiceSuccess(long newServiceId) {

        t.success("提交成功");

        Event event = new Event(EventActionCode.SERVICE_SUBMIT);
        event.addObj(ParamKey.BackData.getValue(), newServiceId);
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
