package com.xhtt.hiddendangermaster.ui.activity.main;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hg.hollowgoods.constant.HGSystemConfig;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.hg.hollowgoods.util.updateapp.DevelopmentUpdateAPPUtils;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.fragment.companymap.CompanyMapFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.statistics.StatisticsFragment;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.main.KnowledgeBaseFragment;
import com.xhtt.hiddendangermaster.ui.fragment.profile.ProfileFragment;
import com.xhtt.hiddendangermaster.ui.fragment.work.WorkFragment;
import com.xhtt.hiddendangermaster.util.UpdateAPPUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @ViewInject(value = R.id.bottom_navigation_bar)
    private BottomNavigationBar bottomNavigationBar;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        // 添加tab标签页
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_main_work_c, "工作")
                .setInactiveIconResource(R.drawable.ic_main_work_u)
        );
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_main_statistics_c, "统计")
                .setInactiveIconResource(R.drawable.ic_main_statistics_u)
        );
        switch (SystemConfig.APP_STYLE) {
            case XHTT_Map:
                bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_main_map_c, "地图")
                        .setInactiveIconResource(R.drawable.ic_main_map_u)
                );
                break;
            default:
                bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_main_knowledge_c, "知识库")
                        .setInactiveIconResource(R.drawable.ic_main_knowledge_u)
                );
                break;
        }
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_main_mine_c, "我的")
                .setInactiveIconResource(R.drawable.ic_main_mine_u)
        );
        bottomNavigationBar.initialise();

        // 首次进入不会主动回调选中页面的监听
        // 所以这里自己调用一遍，初始化第一个页面
        onTabSelected(0);

        new UpdateAPPUtils(baseUI.getBaseContext()).checkUpdate(false);
    }

    @Override
    public void initViewDelay() {
        new DevelopmentUpdateAPPUtils(baseUI.getBaseContext()).checkUpdate();
    }

    @Override
    public void setListener() {

        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // 每次添加之前隐藏所有正在显示的Fragment
        // 然后如果是第一次添加的话使用transaction.add();
        // 但第二次显示的时候,使用transaction.show();
        // 这样子我们就可以保存Fragment的状态了

        hideFragment(transaction);

        switch (position) {
            case 0:
                if (fragment1 == null) {
                    fragment1 = new WorkFragment();
                    transaction.add(R.id.layFrame, fragment1);
                    fragments.add(fragment1);
                } else {
                    transaction.show(fragment1);
                }
                break;
            case 1:
                if (fragment2 == null) {
                    fragment2 = new StatisticsFragment();
                    transaction.add(R.id.layFrame, fragment2);
                    fragments.add(fragment2);
                } else {
                    transaction.show(fragment2);
                }
                break;
            case 2:
                if (fragment3 == null) {
                    switch (SystemConfig.APP_STYLE) {
                        case XHTT_Map:
                            fragment3 = new CompanyMapFragment();
                            break;
                        default:
                            fragment3 = new KnowledgeBaseFragment();
                            break;
                    }
                    transaction.add(R.id.layFrame, fragment3);
                    fragments.add(fragment3);
                } else {
                    transaction.show(fragment3);
                }
                break;
            case 3:
                if (fragment4 == null) {
                    fragment4 = new ProfileFragment();
                    transaction.add(R.id.layFrame, fragment4);
                    fragments.add(fragment4);
                } else {
                    transaction.show(fragment4);
                }
                break;
        }

        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * @param transaction transaction
     */
    public void hideFragment(FragmentTransaction transaction) {

        for (Fragment fragment : fragments) {
            transaction.hide(fragment);
        }
    }

    @Override
    public void onBackPressed() {

        if (HGSystemConfig.IS_DEBUG_MODEL) {
            super.onBackPressed();
        } else {
            if (baseUI.checkBackPressed()) {
                super.onBackPressed();
            }
        }
    }

}
