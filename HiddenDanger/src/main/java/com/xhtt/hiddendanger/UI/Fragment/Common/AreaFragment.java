package com.xhtt.hiddendanger.UI.Fragment.Common;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.UI.Base.BaseMVPFragment;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Base.ListDataFinishHelper;
import com.hg.hollowgoods.UI.Base.ListDataHelper;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendanger.Adapter.AreaAdapter;
import com.xhtt.hiddendanger.Bean.Common.CommonChooseItem;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.Common.Contract.AreaContract;
import com.xhtt.hiddendanger.UI.Fragment.Common.Presenter.AreaPresenter;

import java.util.ArrayList;

/**
 * 地区碎片
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaFragment extends BaseMVPFragment<AreaPresenter> implements AreaContract.View, ListDataHelper {

    private HGRefreshLayout refreshLayout;

    private AreaAdapter adapter;
    private ArrayList<CommonChooseItem> data = new ArrayList<>();
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean isSearch = false;
    private String searchKey = "";
    private int oldDataSize = 0;
    private long parentId = -1;
    private OnCommonCheckedListener onCommonCheckedListener;

    @Override
    public int bindLayout() {
        return R.layout.fragment_area;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);

        adapter = new AreaAdapter(baseUI.getBaseContext(), R.layout.item_area, data);

        refreshLayout.initRecyclerView();
        refreshLayout.addItemDecoration(R.color.line);
        refreshLayout.setAdapter(adapter);

        return null;
    }

    @Override
    public void initViewDelay() {
        if (parentId != -1) {
            getData(parentId);
        }
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

        refreshLayout.getRefreshLayout().setOnMultiPurposeListener(new ListDataFinishHelper() {
            @Override
            public void onAnimFinish(boolean isSuccess) {
                if (isSuccess) {
                    if (data.size() < pageSize || data.size() % pageSize != 0 || oldDataSize == data.size()) {
                        refreshLayout.getRefreshLayout().setNoMoreData(true);
                    } else {
                        refreshLayout.getRefreshLayout().setNoMoreData(false);
                    }

                    isRefresh = false;
                    isLoadMore = false;
                    isSearch = false;

                    oldDataSize = data.size();
                }
            }
        });

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                if (position != adapter.getCheckedPosition()) {
                    adapter.setCheckedPosition(position);
                    adapter.refreshData(data);

                    if (onCommonCheckedListener != null) {
                        onCommonCheckedListener.onAreaChecked(data.get(position));
                    }
                }
            }
        });
    }

    @Override
    public AreaPresenter createPresenter() {
        return new AreaPresenter(getActivity());
    }

    @Override
    public void onSearched(String searchKey) {
        doSearch(searchKey);
    }

    @Override
    public void doRefresh() {
        if (!isRefresh && !isLoadMore && !isSearch) {
            isRefresh = true;
            pageIndex = 1;
            oldDataSize = 0;
            getData(parentId);
        }
    }

    @Override
    public void doLoadMore() {
        if (!isRefresh && !isLoadMore && !isSearch) {
            isLoadMore = true;
            pageIndex++;
            getData(parentId);
        }
    }

    @Override
    public void doSearch(String searchKey) {
        if (!isRefresh && !isLoadMore && !isSearch) {
            isSearch = true;
            pageIndex = 1;
            oldDataSize = 0;
            this.searchKey = searchKey;
            refreshLayout.getRefreshLayout().autoRefreshAnimationOnly();
            new Handler().postDelayed(() -> getData(parentId), 500);
        }
    }

    public CommonChooseItem getCheckedItem() {

        if (adapter == null) {
            return null;
        }

        if (adapter.getCheckedPosition() == -1) {
            return null;
        }

        return data.get(adapter.getCheckedPosition());
    }

    public void setOnCommonCheckedListener(OnCommonCheckedListener onCommonCheckedListener) {
        this.onCommonCheckedListener = onCommonCheckedListener;
    }

    public void clearData() {
        this.parentId = -1;
        data.clear();
        if (adapter != null) {
            adapter.setCheckedPosition(-1);
            adapter.refreshData(data);
        }
    }

    public void getData(long parentId) {
        this.parentId = parentId;
        if (adapter != null) {
            adapter.setCheckedPosition(-1);
        }
        if (mPresenter != null) {
            isRefresh = true;
            mPresenter.getData(pageIndex, pageSize, searchKey, parentId);
        }
    }

    @Override
    public void getDataSuccess(ArrayList<CommonChooseItem> tempData) {

        if (isRefresh || isSearch) {
            data.clear();
        }

        if (tempData != null) {
            data.addAll(tempData);
        }

        baseUI.getBaseContext().runOnUiThread(() -> adapter.refreshData(data));
    }

    @Override
    public void getDataError(Object msg) {
        t.errorByObj(msg);
    }

    @Override
    public void getDataFinish() {
        if (isRefresh) {
            refreshLayout.getRefreshLayout().finishRefresh();
        } else if (isLoadMore) {
            refreshLayout.getRefreshLayout().finishLoadMore();
        } else if (isSearch) {
            refreshLayout.getRefreshLayout().finishRefresh();
        }
    }
}
