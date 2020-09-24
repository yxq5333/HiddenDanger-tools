package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.Widget.SmartRefreshLayout.constant.RefreshState;
import com.xhtt.hiddendanger.Adapter.HiddenDanger.CompanyListAdapter;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;

import java.util.ArrayList;

/**
 * 企业信息界面
 *
 * @author HG
 */

public class CompanyListActivity extends BaseMVPActivity<CompanyListPresenter> implements CompanyListContract.View {

    private final int DIALOG_CODE_ASK_DELETE_DATA = 1000;
    private final int DIALOG_CODE_DELETE_DATA = 1001;

    private HGRefreshLayout refreshLayout;
    private FloatingActionButton add;
    private View noDataView;

    private CompanyListAdapter adapter;
    private ArrayList<Company> data = new ArrayList<>();
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private boolean isSearch = false;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_company_list;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_company_list);
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        refreshLayout = findViewById(R.id.hgRefreshLayout);
        baseUI.initSearchView(refreshLayout, true);
        baseUI.setSearchHint("企业名称");

        new Handler().postDelayed(() -> {

            add = findViewById(R.id.fab_add);

            noDataView = View.inflate(baseUI.getBaseContext(), R.layout.no_data_view_activity_company_list, null);
            baseUI.addNoDataView(noDataView);

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new CompanyListAdapter(baseUI.getBaseContext(), R.layout.item_company_list, data));

            refreshLayout.getRefreshLayout().autoRefresh();

            mPresenter.getHiddenLevel();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);

        return this;
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

            refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

            add.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(CompanyDetailActivity.class,
                            new String[]{ParamKey.WorkType.getValue()},
                            new Object[]{WorkType.Add}
                    );
                }
            });

            adapter.setOnButtonClickListener(new OnRecyclerViewItemClickListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    clickPosition = position;

                    if (view.getId() == R.id.cv_all
                            || view.getId() == R.id.btn_hiddenDangerList) {
                        baseUI.startMyActivity(HiddenDangerListActivity.class,
                                new String[]{ParamKey.ParentData.getValue()},
                                new Object[]{data.get(clickPosition)}
                        );
                    } else if (view.getId() == R.id.btn_edit) {
                        data.get(clickPosition).setShowOther(true);
                        baseUI.startMyActivity(CompanyDetailActivity.class,
                                new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue()},
                                new Object[]{data.get(clickPosition), WorkType.Edit}
                        );
                    } else if (view.getId() == R.id.btn_record) {
                        baseUI.startMyActivity(RecordListActivity.class,
                                new String[]{ParamKey.ParentData.getValue()},
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
                        baseUI.baseDialog.showAlertDialog(R.string.tips_best, sb.toString(), DIALOG_CODE_ASK_DELETE_DATA);
                    }
                }
            });

            baseUI.baseDialog.setOnDialogClickListener((code, result, backData) -> {

                if (result) {
                    switch (code) {
                        case DIALOG_CODE_ASK_DELETE_DATA:
                            baseUI.baseDialog.showProgressDialog("删除中，请稍候……", DIALOG_CODE_DELETE_DATA);
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
    public void onEventUI(Event item) {

        switch (item.getEventActionCode()) {
            case EventActionCode.COMPANY_SUBMIT:
                Company company = item.getObj(ParamKey.Company.getValue(), null);

                if (company != null) {
                    WorkType workType = item.getObj(ParamKey.WorkType.getValue(), WorkType.Detail);

                    if (workType != null) {
                        switch (workType) {
                            case Add:
                                data.add(0, company);
                                adapter.addData(data, 0, 1);
                                baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
                                new Handler().postDelayed(() -> refreshLayout.getRecyclerView().smoothScrollToPosition(0), 500);
                                break;
                            case Edit:
                                data.set(clickPosition, company);
                                adapter.refreshData(data, clickPosition);
                                break;
                        }
                    }
                }
                break;
            case EventActionCode.SERVICE_SUBMIT:
                long newServiceId = item.getObj(ParamKey.BackData.getValue(), data.get(clickPosition).getServiceId());
                data.get(clickPosition).setServiceId(newServiceId);
                data.get(clickPosition).setTimes(data.get(clickPosition).getTimes() + 1);
                break;
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

        if (isSearch && data.size() == 0) {
            TextView tips = noDataView.findViewById(R.id.tv_tips);
            tips.setText("未找到相关数据");
        }

        if (data.size() > 0) {
            baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
        } else {
            baseUI.setDataMode(baseUI.DATA_MODE_NO_DATA);
        }

        new Handler().postDelayed(() -> {

            if (refreshLayout.getRefreshLayout().getState() == RefreshState.Refreshing || refreshLayout.getRefreshLayout().getState() == RefreshState.RefreshReleased) {
                refreshLayout.getRefreshLayout().finishRefresh();
            }

            if (refreshLayout.getRefreshLayout().getState() == RefreshState.Loading) {
                if (refreshLayout.getRefreshLayout().isNoMoreData()) {
                    refreshLayout.getRefreshLayout().finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.getRefreshLayout().finishLoadMore();
                }
            }
        }, SystemConfig.DELAY_TIME_REFRESH_DATA);
    }

    @Override
    public void deleteDataSuccess() {
        t.success("删除成功");
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
