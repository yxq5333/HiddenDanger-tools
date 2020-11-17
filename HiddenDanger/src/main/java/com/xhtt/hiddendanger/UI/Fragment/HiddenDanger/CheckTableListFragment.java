package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.UI.Base.BaseMVPFragment;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.StringUtils;
import com.hg.hollowgoods.Widget.SmartRefreshLayout.constant.RefreshState;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendanger.Adapter.HiddenDanger.CheckTableListAdapter;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CheckTable;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Activity.HiddenDanger.CheckTableDetailActivity;

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
    private View noDataView;

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
    public int bindLayout() {
        return R.layout.fragment_check_table_list;
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

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);

        baseUI.initSearchView(refreshLayout, true);
        baseUI.setSearchHint("检查表名称");
        noDataView = View.inflate(baseUI.getBaseContext(), R.layout.no_data_view_fragment_hidden_danger_list, null);
        baseUI.addNoDataView(noDataView);

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new CheckTableListAdapter(baseUI.getBaseContext(), R.layout.item_check_table_list, data));

        refreshLayout.getRefreshLayout().autoRefresh();

        return this;
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                clickPosition = position;

                baseUI.startMyActivity(CheckTableDetailActivity.class,
                        new String[]{ParamKey.ParentData.getValue(), ParamKey.GrandData.getValue()},
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
                    baseUI.baseDialog.showAlertDialog(R.string.tips_best, sb.toString(), DIALOG_CODE_ASK_DELETE_DATA);
                }

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
            }
        });
    }

    @Override
    public CheckTableListPresenter createPresenter() {
        return new CheckTableListPresenter();
    }

    @Override
    public void onEventUI(Event item) {

        switch (item.getEventActionCode()) {
            case EventActionCode.CHANGE_CHECK_TABLE_CONTENT_STATUS:
                if (data.get(clickPosition).getStatus() != CheckTable.STATUS_CHECK_ING) {
                    data.get(clickPosition).setStatus(CheckTable.STATUS_CHECK_ING);

                    adapter.refreshData(data, clickPosition);
                }
                break;
            case EventActionCode.CHECK_TABLE_SUBMIT:
                if (data.get(clickPosition).getStatus() != CheckTable.STATUS_CHECKED) {
                    data.get(clickPosition).setStatus(CheckTable.STATUS_CHECKED);
                    data.get(clickPosition).setCheckDate(StringUtils.getDateTimeString(System.currentTimeMillis(), StringUtils.DateFormatMode.LINE_YMD));

                    adapter.refreshData(data, clickPosition);
                }
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

}
