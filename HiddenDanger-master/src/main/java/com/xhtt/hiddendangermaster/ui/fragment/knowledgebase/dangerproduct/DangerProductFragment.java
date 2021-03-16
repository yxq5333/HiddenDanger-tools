package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.dangerproduct;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.knowledgebase.dangerproduct.DangerProductAdapter;
import com.xhtt.hiddendangermaster.bean.knowledgebase.dangerproduct.DangerProduct;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct.DangerProductActivity;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct.DangerProductDetailActivity;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPFragment;

import java.util.ArrayList;

/**
 * 危化品安全信息界面
 *
 * @author HG
 */

public class DangerProductFragment extends HDBaseMVPFragment<DangerProductPresenter> implements DangerProductContract.View {

    private ZRefreshLayout refreshLayout;

    private DangerProductAdapter adapter;
    private ArrayList<DangerProduct> data = new ArrayList<>();
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private boolean isSearch = false;

    @Override
    public int bindLayout() {
        return R.layout.fragment_danger_product;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.ZRefreshLayout);

        baseUI.initSearchView(refreshLayout, true);
        baseUI.setStatus(ZStatusLayout.Status.NoData);

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new DangerProductAdapter(baseUI.getBaseContext(), R.layout.item_danger_product, data));

//        refreshLayout.getRefreshLayout().autoRefresh();
    }

    @Override
    public void initViewDelay() {
        TextView tips = baseUI.findViewById(R.id.zStatusLayout).findViewById(R.id.tv_tips);
        tips.setText(TextUtils.isEmpty(searchKey) ? "请输入关键字并搜索" : "未找到相关数据");
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

        adapter.setOnItemClickListener(new ZOnRecyclerViewItemClickOldListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                baseUI.startMyActivity(DangerProductDetailActivity.class,
                        new Enum[]{ParamKey.ParentData},
                        new Object[]{data.get(position)}
                );
            }
        });
    }

    @Override
    public DangerProductPresenter createPresenter() {
        return new DangerProductPresenter();
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
                ((DangerProductActivity) baseUI.getBaseContext()).searchTypeFilter
        );
    }

    @Override
    public void getDataSuccess(ArrayList<DangerProduct> tempData) {

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
            baseUI.setStatus(ZStatusLayout.Status.Default);
        } else {
            baseUI.setStatus(ZStatusLayout.Status.NoData);
        }

        if (isSearch && data.size() == 0) {
            TextView tips = baseUI.findViewById(R.id.tv_tips);
            tips.setText(TextUtils.isEmpty(searchKey) ? "请输入关键字并搜索" : "未找到相关数据");
        }

        new Handler().postDelayed(() -> {

            if (refreshLayout.getRefreshLayout().getState() == RefreshState.Refreshing || refreshLayout.getRefreshLayout().getState() == RefreshState.RefreshReleased) {
                refreshLayout.getRefreshLayout().finishRefresh();
            }

            if (refreshLayout.getRefreshLayout().getState() == RefreshState.Loading) {
//                if (refreshLayout.getRefreshLayout().isNoMoreData()) {
//                    refreshLayout.getRefreshLayout().finishLoadMoreWithNoMoreData();
//                } else {
                refreshLayout.getRefreshLayout().finishLoadMore();
//                }
            }
        }, SystemConfig.DELAY_TIME_REFRESH_DATA);
    }

}
