package com.xhtt.hiddendangermaster.ui.fragment.work;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.ui.base.BaseFragment;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.hg.hollowgoods.widget.smartrefresh.SmartRefreshLayout;
import com.hg.hollowgoods.widget.smartrefresh.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.knowledgebase.accidentcase.AccidentCase;
import com.xhtt.hiddendangermaster.bean.knowledgebase.banner.Banner;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.CheckTableChangeCompanyListActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.CheckTableListActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.CompanyDetailActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.CompanyListActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.FreeTakeActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct.DangerProductActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.laws.LawsActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds.MSDSActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.technologystandard.TechnologyStandardActivity;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.accidentcase.AccidentCaseContract;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.accidentcase.AccidentCaseFragment;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.banner.BannerFragment;

import java.util.ArrayList;

/**
 * 工作界面
 *
 * @author HG
 */

public class WorkFragment extends BaseFragment {

    private SmartRefreshLayout smartRefreshLayout;
    private BannerFragment bannerFragment;
    private AccidentCaseFragment accidentCaseFragment;
    private View tab1;
    private View tab2;
    private View tab3;
    private View tab5;
    private View tab6;
    private View tab7;
    private View tab8;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_work;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);
        baseUI.setStatus(HGStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            smartRefreshLayout = baseUI.findViewById(R.id.smartRefreshLayout);
            tab1 = baseUI.findViewById(R.id.tab1);
            tab2 = baseUI.findViewById(R.id.tab2);
            tab3 = baseUI.findViewById(R.id.tab3);
            tab5 = baseUI.findViewById(R.id.tab5);
            tab6 = baseUI.findViewById(R.id.tab6);
            tab7 = baseUI.findViewById(R.id.tab7);
            tab8 = baseUI.findViewById(R.id.tab8);

            smartRefreshLayout.setEnableHeaderTranslationContent(false);
            smartRefreshLayout.setEnableAutoLoadMore(false);
            smartRefreshLayout.setPrimaryColorsId(R.color.colorPrimary, R.color.white);

            // 4个按钮部分
            initTab();

            // 知识库扩展部分
            initTab2();

            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            // Banner部分
            bannerFragment = new BannerFragment();
            bannerFragment.setFragmentArguments(
                    "1",
                    new Enum[]{ParamKey.Location, ParamKey.BannerType},
                    new Object[]{Banner.LOCATION_MAIN_ACTIVITY, BannerFragment.BANNER_TYPE_HIDDEN_DANGER}
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

            tab1.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(CompanyListActivity.class);
                }
            });

            tab2.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(FreeTakeActivity.class);
                }
            });

            tab3.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(CheckTableChangeCompanyListActivity.class);
                }
            });

            tab5.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(LawsActivity.class);
                }
            });

            tab6.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(TechnologyStandardActivity.class);
                }
            });

            tab7.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(DangerProductActivity.class);
                }
            });

            tab8.setOnClickListener(new OnViewClickListener(false) {
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
                public void getDataFinish() {

                    if (smartRefreshLayout.getState() == RefreshState.Refreshing || smartRefreshLayout.getState() == RefreshState.RefreshReleased) {
                        smartRefreshLayout.finishRefresh();
                    }

                    if (smartRefreshLayout.getState() == RefreshState.Loading) {
                        if (smartRefreshLayout.isNoMoreData()) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            smartRefreshLayout.finishLoadMore();
                        }
                    }
                }

                @Override
                public void getDataNoMore() {
                    baseUI.getBaseContext().runOnUiThread(() -> smartRefreshLayout.setNoMoreData(true));
                }
            });

            baseUI.setStatus(HGStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public void onEventUI(HGEvent item) {
        if (item.isFromMe(this.getClass().getName())) {
            Company company;

            if (item.getEventActionCode() == EventActionCode.COMPANY_SELECTOR) {
                String companyName = item.getObj(ParamKey.StringData, "");
                company = item.getObj(ParamKey.Company, null);

                if (!TextUtils.isEmpty(companyName)) {
                    company = new Company();
                    company.setCompanyName(companyName);
                    company.setShowOther(false);
                    baseUI.startMyActivity(CompanyDetailActivity.class,
                            new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.FromClass},
                            new Object[]{company, WorkType.Add, this.getClass()}
                    );
                } else {
                    baseUI.startMyActivity(CheckTableListActivity.class,
                            new Enum[]{ParamKey.ParentData},
                            new Object[]{company}
                    );
                }
            } else if (item.getEventActionCode() == EventActionCode.COMPANY_SUBMIT) {
                company = item.getObj(ParamKey.Company, null);
                baseUI.startMyActivity(CheckTableListActivity.class,
                        new Enum[]{ParamKey.ParentData},
                        new Object[]{company}
                );
            }
        }
    }

    private void initTab() {

        View[] tabs = {
                tab2,
                tab3,
        };
        String[] labels = {
                "随拍随记",
                "检查表",
        };
        int[] bgRes = {
                R.drawable.box_yellow,
                R.drawable.box_blue,
        };
        int[] iconRes = {
                R.drawable.ic_free_take,
                R.drawable.ic_check_table,
        };
        for (int i = 0; i < tabs.length; i++) {
            TextView tv = tabs[i].findViewById(R.id.tv_label);
            tv.setText(labels[i]);

            ImageView iv = tabs[i].findViewById(R.id.iv_img);
            iv.setBackgroundResource(bgRes[i]);
            iv.setImageResource(iconRes[i]);
        }
    }

    private void initTab2() {

        View[] tabs = {
                tab5,
                tab6,
                tab7,
                tab8,
        };
        String[] labels = {
                "法律法规",
                "技术标准",
                "危化品信息",
                "MSDS",
        };
        int[] iconRes = {
                R.mipmap.ic_laws2,
                R.mipmap.ic_technology_standard2,
                R.mipmap.ic_danger_product2,
                R.mipmap.ic_msds2,
        };
        for (int i = 0; i < tabs.length; i++) {
            TextView tv = tabs[i].findViewById(com.xhtt.hiddendangermaster.R.id.tv_label);
            tv.setText(labels[i]);

            ImageView iv = tabs[i].findViewById(com.xhtt.hiddendangermaster.R.id.iv_img);
            iv.setImageResource(iconRes[i]);
        }
    }

}
