package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.accidentcase.AccidentCase;
import com.xhtt.hiddendangermaster.bean.knowledgebase.banner.Banner;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct.DangerProductActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.laws.LawsActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds.MSDSActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.technologystandard.TechnologyStandardActivity;
import com.xhtt.hiddendangermaster.ui.base.HDBaseFragment;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.accidentcase.AccidentCaseContract;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.accidentcase.AccidentCaseFragment;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.banner.BannerFragment;

import java.util.ArrayList;

/**
 * 主界面
 * Created by Hollow Goods on 2019-05-15.
 */
public class KnowledgeBaseFragment extends HDBaseFragment {

    private SmartRefreshLayout smartRefreshLayout;
    private BannerFragment bannerFragment;
    private AccidentCaseFragment accidentCaseFragment;
    private View tab1;
    private View tab2;
    private View tab3;
    private View tab4;

    @Override
    public int bindLayout() {
        return R.layout.fragment_knowledge_base;
    }

    @Override
    public void initView(View view, Bundle bundle) {

        baseUI.setCommonTitleViewVisibility(false);
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {
            smartRefreshLayout = baseUI.findViewById(R.id.smartRefreshLayout);
            tab1 = baseUI.findViewById(R.id.tab1);
            tab2 = baseUI.findViewById(R.id.tab2);
            tab3 = baseUI.findViewById(R.id.tab3);
            tab4 = baseUI.findViewById(R.id.tab4);

            smartRefreshLayout.setEnableHeaderTranslationContent(false);
            smartRefreshLayout.setEnableAutoLoadMore(false);
            smartRefreshLayout.setPrimaryColorsId(R.color.colorPrimary, R.color.white);

            // 4个按钮部分
            initTab();

            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            // Banner部分
            bannerFragment = new BannerFragment();
            bannerFragment.setFragmentArguments(
                    "1",
                    new Enum[]{ParamKey.Location, ParamKey.BannerType},
                    new Object[]{Banner.LOCATION_CONTENT, BannerFragment.BANNER_TYPE_KNOWLEDGE_BASE}
            );
            ft.add(R.id.fl_banner, bannerFragment);
            // 事故案例部分
            ft.add(R.id.fl_accidentCase, accidentCaseFragment = new AccidentCaseFragment());

            ft.commitAllowingStateLoss();

            smartRefreshLayout.autoRefresh();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            tab1.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(LawsActivity.class);
                }
            });

            tab2.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(TechnologyStandardActivity.class);
                }
            });

            tab3.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(DangerProductActivity.class);
                }
            });

            tab4.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(MSDSActivity.class);
                }
            });

            smartRefreshLayout.setOnRefreshListener(refreshLayout -> accidentCaseFragment.doRefresh());

            smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> accidentCaseFragment.doLoadMore());

            accidentCaseFragment.setListenerView(new AccidentCaseContract.View() {
                @Override
                public void getDataSuccess(ArrayList<AccidentCase> tempData) {

                }

                @Override
                public void getDataError() {

                }

                @Override
                public void getDataNoMore() {
                    baseUI.getBaseContext().runOnUiThread(() -> smartRefreshLayout.setNoMoreData(true));
                }

                @Override
                public void getDataFinish() {

                    if (smartRefreshLayout.getState() == RefreshState.Refreshing || smartRefreshLayout.getState() == RefreshState.RefreshReleased) {
                        smartRefreshLayout.finishRefresh();
                    }

                    if (smartRefreshLayout.getState() == RefreshState.Loading) {
//                        if (smartRefreshLayout.isNoMoreData()) {
//                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
//                        } else {
                        smartRefreshLayout.finishLoadMore();
//                        }
                    }
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    private void initTab() {

        View[] tabs = {
                tab1,
                tab2,
                tab3,
                tab4,
        };
        String[] labels = {
                "法律法规",
                "技术标准",
                "危化品信息",
                "MSDS",
        };
        int[] bgRes = {
                R.drawable.ball_blue,
                R.drawable.ball_yellow,
                R.drawable.ball_green,
                R.drawable.ball_orange,
        };
        int[] iconRes = {
                R.drawable.ic_laws,
                R.drawable.ic_technology_standard,
                R.drawable.ic_danger_product,
                R.drawable.ic_msds,
        };
        for (int i = 0; i < tabs.length; i++) {
            TextView tv = tabs[i].findViewById(R.id.tv_label);
            tv.setText(labels[i]);

            ImageView iv = tabs[i].findViewById(R.id.iv_img);
            iv.setBackgroundResource(bgRes[i]);
            iv.setImageResource(iconRes[i]);
        }
    }

}
