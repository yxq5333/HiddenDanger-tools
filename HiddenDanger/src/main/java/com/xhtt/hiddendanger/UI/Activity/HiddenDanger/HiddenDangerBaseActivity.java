package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.Widget.SmartRefreshLayout.constant.RefreshState;
import com.xhtt.hiddendanger.Adapter.HiddenDanger.HiddenDangerBaseAdapter;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerListContract;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.HiddenDangerListPresenter;
import com.xhtt.knowledgebase.UI.Activity.MSDS.MSDSActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 隐患库界面
 *
 * @author HG
 */

public class HiddenDangerBaseActivity extends BaseMVPActivity<HiddenDangerListPresenter> implements HiddenDangerListContract.View {

    private HGRefreshLayout refreshLayout;
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
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger_base;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, "隐患库");
        baseUI.setCommonRightTitleText("MSDS");
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        new Handler().postDelayed(() -> {

            refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);
            status = baseUI.findViewById(R.id.rg_status);

            baseUI.initSearchView(refreshLayout, true);
            baseUI.setSearchHint("隐患描述");

            ((RadioButton) status.getChildAt(0)).setChecked(true);

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new HiddenDangerBaseAdapter(baseUI.getBaseContext(), R.layout.item_hidden_danger_base, data));

            refreshLayout.getRefreshLayout().autoRefresh();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);

        return null;
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

            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                    clickPosition = position;
                    backData();
                }
            });

            baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public HiddenDangerListPresenter createPresenter() {
        return new HiddenDangerListPresenter();
    }

    @Override
    public void onRightTitleClick(View view, int id) {
        baseUI.startMyActivity(MSDSActivity.class,
                new String[]{ParamKey.StringData.getValue()},
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
                if (refreshLayout.getRefreshLayout().isNoMoreData()) {
                    refreshLayout.getRefreshLayout().finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.getRefreshLayout().finishLoadMore();
                }
            }
        }, SystemConfig.DELAY_TIME_REFRESH_DATA);
    }

    private void backData() {

        Event event = new Event(EventActionCode.SELECTOR_HIDDEN_DANGER_STORE);
        event.addObj(ParamKey.BackData.getValue(), data.get(clickPosition));
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

}
