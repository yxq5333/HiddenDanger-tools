package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger.HiddenDangerBaseAdapter;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds.MSDSActivity;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerListContract;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.HiddenDangerListPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 隐患库界面
 *
 * @author HG
 */

public class HiddenDangerBaseActivity extends HDBaseMVPActivity<HiddenDangerListPresenter> implements HiddenDangerListContract.View {

    private ZRefreshLayout refreshLayout;
    private RadioGroup status;

    private HiddenDangerBaseAdapter adapter;
    private ArrayList<HiddenDanger> data = new ArrayList<>();
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private boolean isOnlyMine = false;

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger_base;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), "隐患库");
        baseUI.setCommonRightTitleText("MSDS");
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            refreshLayout = baseUI.findViewById(R.id.ZRefreshLayout);
            status = baseUI.findViewById(R.id.rg_status);

            baseUI.initSearchView(refreshLayout, true);
            baseUI.setSearchHint("隐患描述");

            ((RadioButton) status.getChildAt(0)).setChecked(true);

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new HiddenDangerBaseAdapter(baseUI.getBaseContext(), R.layout.item_hidden_danger_base, data));

            refreshLayout.getRefreshLayout().autoRefresh();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

            refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

            status.setOnCheckedChangeListener((group, checkedId) -> {

                if (checkedId == R.id.rb_all) {
                    isOnlyMine = false;
                } else if (checkedId == R.id.rb_mine) {
                    isOnlyMine = true;
                }

                refreshLayout.getRefreshLayout().autoRefresh();
            });

            adapter.setOnItemClickListener(new ZOnRecyclerViewItemClickOldListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                    clickPosition = position;
                    backData();
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public HiddenDangerListPresenter createPresenter() {
        return new HiddenDangerListPresenter();
    }

    @Override
    public void onRightTitleClick(View view, int id) {
        baseUI.startMyActivity(MSDSActivity.class,
                new Enum[]{ParamKey.StringData},
                new Object[]{searchKey}
        );
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
        mPresenter.getStoreData(pageIndex, pageSize, searchKey, isOnlyMine);
    }

    @Override
    public void onSearched(String searchKey) {
        this.searchKey = searchKey.trim();
        doRefresh();
    }

    @Override
    public void getDataSuccess(ArrayList<HiddenDanger> tempData) {

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

    private void backData() {

        ZEvent event = new ZEvent(EventActionCode.SELECTOR_HIDDEN_DANGER_STORE);
        event.addObj(ParamKey.BackData, data.get(clickPosition));
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

}
