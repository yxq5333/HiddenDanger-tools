package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.msds;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.ui.base.click.OnRecyclerViewItemClickOldListener;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPFragment;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.hg.hollowgoods.widget.smartrefresh.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.knowledgebase.msds.MSDSAdapter;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDS;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds.MSDSActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds.MSDSDetailActivity;

import java.util.ArrayList;

/**
 * MSDS界面
 *
 * @author HG
 */

public class MSDSFragment extends BaseMVPFragment<MSDSPresenter> implements MSDSContract.View {

    private HGRefreshLayout refreshLayout;

    private MSDSAdapter adapter;
    private ArrayList<MSDS> data = new ArrayList<>();
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private boolean isSearch = false;

    @Override
    public int bindLayout() {
        return R.layout.fragment_msds;
    }

    @Override
    public void initParamData() {
        searchKey = baseUI.getParam(ParamKey.StringData, "");
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);

        baseUI.initSearchView(refreshLayout, true);
        baseUI.setSearchText(searchKey);
        baseUI.setStatus(HGStatusLayout.Status.NoData);

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new MSDSAdapter(baseUI.getBaseContext(), R.layout.item_msds, data));

        if (!TextUtils.isEmpty(searchKey)) {
            doSearch();
        }
    }

    @Override
    public void initViewDelay() {
        TextView tips = baseUI.findViewById(R.id.hgStatusLayout).findViewById(R.id.tv_tips);
        tips.setText(TextUtils.isEmpty(searchKey) ? "请输入关键字并搜索" : "搜索中……");
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickOldListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                baseUI.startMyActivity(MSDSDetailActivity.class,
                        new Enum[]{ParamKey.ParentData},
                        new Object[]{data.get(position)}
                );
            }
        });
    }

    @Override
    public MSDSPresenter createPresenter() {
        return new MSDSPresenter();
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

    public void doSearch() {
        isSearch = true;
        isLoadMore = false;
        pageIndex = 1;
        refreshLayout.getRefreshLayout().resetNoMoreData();
        refreshLayout.getRefreshLayout().autoRefreshAnimationOnly();
        getData();
    }

    @Override
    public void onSearched(String searchKey) {
        this.searchKey = searchKey.trim();
        doSearch();
    }

    private void getData() {
        mPresenter.getData(
                pageIndex,
                pageSize,
                TextUtils.isEmpty(searchKey) ? Constants.DEFAULT_SEARCH_KEY : searchKey,
                ((MSDSActivity) baseUI.getBaseContext()).searchTypeFilter
        );
    }

    @Override
    public void getDataSuccess(ArrayList<MSDS> tempData) {

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
            tips.setText(TextUtils.isEmpty(searchKey) ? "请输入关键字并搜索" : "未找到相关数据");
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
}
