package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.Widget.SmartRefreshLayout.constant.RefreshState;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendanger.Adapter.HiddenDanger.RecordListAdapter;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Record;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.R;

import java.util.ArrayList;

/**
 * 服务记录界面
 *
 * @author HG
 */

public class RecordListActivity extends BaseMVPActivity<RecordListPresenter> implements RecordListContract.View {

    private HGRefreshLayout refreshLayout;
    private View noDataView;

    private RecordListAdapter adapter;
    private ArrayList<Record> data = new ArrayList<>();
    private Company parentData;
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_record_list;
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

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_record_list);
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        new Handler().postDelayed(() -> {

            noDataView = View.inflate(baseUI.getBaseContext(), R.layout.no_data_view_activity_company_list, null);
            baseUI.addNoDataView(noDataView);
            TextView tips = noDataView.findViewById(R.id.tv_tips);
            tips.setText("暂未提交检查记录");

            refreshLayout = findViewById(R.id.hgRefreshLayout);

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new RecordListAdapter(baseUI.getBaseContext(), R.layout.item_record_list, data));

            refreshLayout.getRefreshLayout().autoRefresh();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);

        return null;
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

            refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    clickPosition = position;

                    baseUI.startMyActivity(HiddenDangerOnceListActivity.class,
                            new String[]{ParamKey.GrandData.getValue(), ParamKey.ParentData.getValue()},
                            new Object[]{parentData, data.get(clickPosition)}
                    );
                }
            });
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public RecordListPresenter createPresenter() {
        return new RecordListPresenter();
    }

    private void doLoadMore() {
        isLoadMore = true;
        pageIndex++;
        getData();
    }

    private void doRefresh() {
        isLoadMore = false;
        pageIndex = 1;
        getData();
    }

    private void getData() {
        mPresenter.getData(parentData.getId(), pageIndex, pageSize);
    }

    @Override
    public void getDataSuccess(ArrayList<Record> tempData) {

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
    protected void onResume() {
        super.onResume();
        if (refreshLayout != null && adapter != null) {
            doRefresh();
        }
    }
}
