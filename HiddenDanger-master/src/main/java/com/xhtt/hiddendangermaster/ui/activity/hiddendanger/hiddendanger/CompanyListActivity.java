package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger.CompanyListAdapter;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;

import java.util.ArrayList;

/**
 * 企业信息界面
 *
 * @author HG
 */

public class CompanyListActivity extends HDBaseMVPActivity<CompanyListPresenter> implements CompanyListContract.View {

    private final int DIALOG_CODE_ASK_DELETE_DATA = 1000;
    private final int DIALOG_CODE_DELETE_DATA = 1001;

    private ZRefreshLayout refreshLayout;
    private FloatingActionButton add;

    private CompanyListAdapter adapter;
    private ArrayList<Company> data = new ArrayList<>();
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private boolean isSearch = false;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_company_list;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_company_list);
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        refreshLayout = findViewById(R.id.ZRefreshLayout);
        baseUI.initSearchView(refreshLayout, true);
        baseUI.setSearchHint("企业名称");

        new Handler().postDelayed(() -> {

            add = findViewById(R.id.fab_add);

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new CompanyListAdapter(baseUI.getBaseContext(), R.layout.item_company_list, data));

            refreshLayout.getRefreshLayout().autoRefresh();

            mPresenter.getHiddenLevel();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

            refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

            add.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(CompanyDetailActivity.class,
                            new Enum[]{ParamKey.WorkType},
                            new Object[]{WorkType.Add}
                    );
                }
            });

            adapter.setOnButtonClickListener(new ZOnRecyclerViewItemClickOldListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    clickPosition = position;

                    if (view.getId() == R.id.cv_all
                            || view.getId() == R.id.btn_hiddenDangerList) {
                        baseUI.startMyActivity(HiddenDangerListActivity.class,
                                new Enum[]{ParamKey.ParentData},
                                new Object[]{data.get(clickPosition)}
                        );
                    } else if (view.getId() == R.id.btn_edit) {
                        data.get(clickPosition).setShowOther(true);
                        baseUI.startMyActivity(CompanyDetailActivity.class,
                                new Enum[]{ParamKey.ParentData, ParamKey.WorkType},
                                new Object[]{data.get(clickPosition), WorkType.Edit}
                        );
                    } else if (view.getId() == R.id.btn_record) {
                        baseUI.startMyActivity(RecordListActivity.class,
                                new Enum[]{ParamKey.ParentData},
                                new Object[]{data.get(clickPosition)}
                        );
                    }
                }

                @Override
                public void onRecyclerViewItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    clickPosition = position;

                    if (view.getId() == R.id.cv_all) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("确定要删除");
                        sb.append("\"");
                        sb.append(data.get(clickPosition).getCompanyName());
                        sb.append("\"");
                        sb.append("吗？");
                        baseUI.baseDialog.showAlertDialog(new ZDialogConfig.AlertConfig(DIALOG_CODE_ASK_DELETE_DATA)
                                .setTitle(R.string.z_tips_best)
                                .setContent(sb.toString())
                        );
                    }
                }
            });

            baseUI.baseDialog.addOnDialogClickListener((code, result, backData) -> {

                if (result) {
                    switch (code) {
                        case DIALOG_CODE_ASK_DELETE_DATA:
                            baseUI.baseDialog.showProgressDialog(new ZDialogConfig.ProgressConfig(DIALOG_CODE_DELETE_DATA)
                                    .setContent("删除中，请稍候……")
                            );
                            mPresenter.deleteData(data.get(clickPosition).getId());
                            break;
                    }
                }
            });

//            noDataView.setOnClickListener(new OnViewClickListener(false) {
//                @Override
//                public void onViewClick(View view, int id) {
//                    t.info("刷新中，请稍候……");
//                    doRefresh();
//                }
//            });

//            baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
//            if (baseUI.getFloatingSearchView().getVisibility() != View.VISIBLE) {
//                baseUI.getFloatingSearchView().setVisibility(View.VISIBLE);
//            }
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public CompanyListPresenter createPresenter() {
        return new CompanyListPresenter();
    }

    @Override
    public void onEventUI(ZEvent item) {
        if (item.getEventActionCode() == EventActionCode.COMPANY_SUBMIT) {
            Company company = item.getObj(ParamKey.Company, null);

            if (company != null) {
                WorkType workType = item.getObj(ParamKey.WorkType, WorkType.Detail);

                if (workType != null) {
                    switch (workType) {
                        case Add:
                            data.add(0, company);
                            adapter.addData(data, 0, 1);
                            baseUI.setStatus(ZStatusLayout.Status.Default);
                            new Handler().postDelayed(() -> refreshLayout.getRecyclerView().smoothScrollToPosition(0), 500);
                            break;
                        case Edit:
                            data.set(clickPosition, company);
                            adapter.refreshData(data, clickPosition);
                            break;
                    }
                }
            }
        } else if (item.getEventActionCode() == EventActionCode.SERVICE_SUBMIT) {
            long newServiceId = item.getObj(ParamKey.BackData, data.get(clickPosition).getServiceId());
            data.get(clickPosition).setServiceId(newServiceId);
            data.get(clickPosition).setTimes(data.get(clickPosition).getTimes() + 1);
        }
    }

    private void doLoadMore() {
        isSearch = false;
        isLoadMore = true;
        pageIndex++;
        getData();
    }

    private void doRefresh() {
        isSearch = false;
        isLoadMore = false;
        pageIndex = 1;
        getData();
    }

    private void doSearch() {
        isSearch = true;
        isLoadMore = false;
        pageIndex = 1;
        refreshLayout.getRefreshLayout().resetNoMoreData();
        refreshLayout.getRefreshLayout().autoRefreshAnimationOnly();
        getData();
    }

    private void getData() {
        mPresenter.getData(pageIndex, pageSize, searchKey);
    }

    @Override
    public void onSearched(String searchKey) {
        this.searchKey = searchKey.trim();
        doSearch();
    }

    @Override
    public void getDataSuccess(ArrayList<Company> tempData) {

        if (!isLoadMore) {
            data.clear();
        }

        if (tempData != null) {
            data.addAll(tempData);

            if (tempData.size() < pageSize) {
                baseUI.getBaseContext().runOnUiThread(() -> refreshLayout.getRefreshLayout().setNoMoreData(true));
            }
        } else {
            baseUI.getBaseContext().runOnUiThread(() -> refreshLayout.getRefreshLayout().setNoMoreData(true));
        }

        runOnUiThread(() -> {
            adapter.refreshData(data);
            getDataFinish();
        });
    }

    @Override
    public void getDataError() {
        refreshLayout.getRefreshLayout().setNoMoreData(true);
    }

    @Override
    public void getDataFinish() {

        if (data.size() > 0) {
            baseUI.setStatus(ZStatusLayout.Status.Default);
        } else {
            baseUI.setStatus(ZStatusLayout.Status.NoData);
        }

        if (isSearch && data.size() == 0) {
            TextView tips = baseUI.findViewById(R.id.tv_tips);
            tips.setText("未找到相关数据");
        }

        new Handler().postDelayed(() -> {

            if (refreshLayout.getRefreshLayout().getState() == RefreshState.Refreshing || refreshLayout.getRefreshLayout().getState() == RefreshState.RefreshReleased) {
                refreshLayout.getRefreshLayout().finishRefresh();
            }

            if (refreshLayout.getRefreshLayout().getState() == RefreshState.Loading) {
//                if (refreshLayout.getRefreshLayout().()) {
//                    refreshLayout.getRefreshLayout().finishLoadMoreWithNoMoreData();
//                } else {
                refreshLayout.getRefreshLayout().finishLoadMore();
//                }
            }
        }, SystemConfig.DELAY_TIME_REFRESH_DATA);
    }

    @Override
    public void deleteDataSuccess() {
        Zt.success("删除成功");
        data.remove(clickPosition);
        adapter.removeData(data, clickPosition, 1);
    }

    @Override
    public void deleteDataError() {

    }

    @Override
    public void deleteDataFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_DELETE_DATA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshLayout != null && adapter != null) {
            refreshLayout.getRefreshLayout().resetNoMoreData();
            doRefresh();
        }
    }
}
