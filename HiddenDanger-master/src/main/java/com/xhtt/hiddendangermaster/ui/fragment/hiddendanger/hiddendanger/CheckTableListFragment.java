package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.ui.base.click.OnRecyclerViewItemClickOldListener;
import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPFragment;
import com.hg.hollowgoods.util.StringUtils;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.hg.hollowgoods.widget.smartrefresh.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger.CheckTableListAdapter;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CheckTable;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.CheckTableDetailActivity;

import java.util.ArrayList;

/**
 * 检查表界面
 *
 * @author HG
 */

public class CheckTableListFragment extends BaseMVPFragment<CheckTableListPresenter> implements CheckTableListContract.View {

    private final int DIALOG_CODE_ASK_DELETE_DATA = 1000;
    private final int DIALOG_CODE_DELETE_DATA = 1001;

    private HGRefreshLayout refreshLayout;

    private CheckTableListAdapter adapter;
    private ArrayList<CheckTable> data = new ArrayList<>();
    private Company parentData;
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
        return R.layout.fragment_check_table_list;
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

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);

        baseUI.initSearchView(refreshLayout, true);
        baseUI.setSearchHint("检查表名称");

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new CheckTableListAdapter(baseUI.getBaseContext(), R.layout.item_check_table_list, data));

        refreshLayout.getRefreshLayout().autoRefresh();
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickOldListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                clickPosition = position;

                baseUI.startMyActivity(CheckTableDetailActivity.class,
                        new Enum[]{ParamKey.ParentData, ParamKey.GrandData},
                        new Object[]{data.get(clickPosition), parentData}
                );
            }

            @Override
            public void onRecyclerViewItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                clickPosition = position;

                if (view.getId() == R.id.cv_all) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("确定要删除");
                    sb.append("\"");
                    sb.append(data.get(clickPosition).getCheckTableName());
                    sb.append("\"");
                    sb.append("吗？");
                    baseUI.baseDialog.showAlertDialog(new DialogConfig.AlertConfig(DIALOG_CODE_ASK_DELETE_DATA)
                            .setTitle(R.string.tips_best)
                            .setText(sb.toString())
                    );
                }

                baseUI.baseDialog.setOnDialogClickListener((code, result, backData) -> {

                    if (result) {
                        switch (code) {
                            case DIALOG_CODE_ASK_DELETE_DATA:
                                baseUI.baseDialog.showProgressDialog(new DialogConfig.ProgressConfig(DIALOG_CODE_DELETE_DATA)
                                        .setText("删除中，请稍候……")
                                );
                                mPresenter.deleteData(data.get(clickPosition).getId());
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    public CheckTableListPresenter createPresenter() {
        return new CheckTableListPresenter();
    }

    @Override
    public void onEventUI(HGEvent item) {
        if (item.getEventActionCode() == EventActionCode.CHANGE_CHECK_TABLE_CONTENT_STATUS) {
            if (data.get(clickPosition).getStatus() != CheckTable.STATUS_CHECK_ING) {
                data.get(clickPosition).setStatus(CheckTable.STATUS_CHECK_ING);

                adapter.refreshData(data, clickPosition);
            }
        } else if (item.getEventActionCode() == EventActionCode.CHECK_TABLE_SUBMIT) {
            if (data.get(clickPosition).getStatus() != CheckTable.STATUS_CHECKED) {
                data.get(clickPosition).setStatus(CheckTable.STATUS_CHECKED);
                data.get(clickPosition).setCheckDate(StringUtils.getDateTimeString(System.currentTimeMillis(), StringUtils.DateFormatMode.LINE_YMD));

                adapter.refreshData(data, clickPosition);
            }
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

    public void doRefresh(Company company) {
        parentData = company;
        refreshLayout.getRefreshLayout().resetNoMoreData();
        refreshLayout.getRefreshLayout().autoRefreshAnimationOnly();
        doRefresh();
    }

    private void getData() {
        mPresenter.getData(parentData.getId(), pageIndex, pageSize, parentData.getServiceId(), searchKey);
    }

    @Override
    public void onSearched(String searchKey) {
        this.searchKey = searchKey.trim();
        doSearch();
    }

    @Override
    public void getDataSuccess(ArrayList<CheckTable> tempData) {

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

        baseUI.getBaseContext().runOnUiThread(() -> {
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
            TextView tips = baseUI.findViewById(R.id.tv_tips);
            tips.setText("未找到相关数据");
        }

        if (data.size() > 0) {
            baseUI.setStatus(HGStatusLayout.Status.Default);
        } else {
            baseUI.setStatus(HGStatusLayout.Status.NoData);
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

}
