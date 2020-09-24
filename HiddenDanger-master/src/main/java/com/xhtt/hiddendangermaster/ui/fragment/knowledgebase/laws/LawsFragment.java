package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.laws;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.ui.base.click.OnRecyclerViewItemClickOldListener;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPFragment;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.hg.hollowgoods.widget.smartrefresh.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.knowledgebase.laws.LawsAdapter;
import com.xhtt.hiddendangermaster.bean.knowledgebase.common.FileDetail;
import com.xhtt.hiddendangermaster.bean.knowledgebase.laws.Laws;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.LawType;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.common.FileDetailActivity;

import java.util.ArrayList;

/**
 * 法律法规界面
 *
 * @author HG
 */

public class LawsFragment extends BaseMVPFragment<LawsPresenter> implements LawsContract.View {

    private HGRefreshLayout refreshLayout;

    private LawsAdapter adapter;
    private ArrayList<Laws> data = new ArrayList<>();
    private LawType lawType;
    private String title;
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private boolean isSearch = false;

    @Override
    public int bindLayout() {
        return R.layout.fragment_laws;
    }

    @Override
    public void initParamData() {
        lawType = baseUI.getParam(ParamKey.LawType, LawType.Legal);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);

        switch (lawType) {
            case Legal:
                baseUI.initSearchView(refreshLayout, true, Constants.HISTORY_CODE_LAWS_LEGAL);
                title = "法律";
                break;
            case Law:
                baseUI.initSearchView(refreshLayout, true, Constants.HISTORY_CODE_LAWS_LAW);
                title = "法规";
                break;
            case Rule:
                baseUI.initSearchView(refreshLayout, true, Constants.HISTORY_CODE_LAWS_RULE);
                title = "规章";
                break;
            case File:
                baseUI.initSearchView(refreshLayout, true, Constants.HISTORY_CODE_LAWS_FILE);
                title = "文件";
                break;
        }

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new LawsAdapter(baseUI.getBaseContext(), R.layout.item_laws, data));

        refreshLayout.getRefreshLayout().autoRefresh();
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickOldListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                FileDetail fileDetail = new FileDetail();
                fileDetail.setActivityTitle(title);
                fileDetail.setTitle(data.get(position).getTitle());
                fileDetail.setContent(data.get(position).getContent());
                fileDetail.setWebFiles(data.get(position).getFileList());
                fileDetail.setMemo(data.get(position).getMemo());

                baseUI.startMyActivity(FileDetailActivity.class,
                        new Enum[]{HGParamKey.AppFiles},
                        new Object[]{fileDetail}
                );
            }
        });
    }

    @Override
    public LawsPresenter createPresenter() {
        return new LawsPresenter();
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

    @Override
    public void onSearched(String searchKey) {
        this.searchKey = searchKey.trim();
        doSearch();
    }

    private void getData() {
        mPresenter.getData(pageIndex, pageSize, searchKey, lawType);
    }

    @Override
    public void getDataSuccess(ArrayList<Laws> tempData) {

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
}
