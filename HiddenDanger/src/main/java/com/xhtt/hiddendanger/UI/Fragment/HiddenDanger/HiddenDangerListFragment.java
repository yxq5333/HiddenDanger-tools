package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGParamKey;
import com.hg.hollowgoods.Constant.HGSystemConfig;
import com.hg.hollowgoods.UI.Base.BaseMVPFragment;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.IP.InterfaceConfig;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.Widget.SmartRefreshLayout.constant.RefreshState;
import com.xhtt.hiddendanger.Adapter.HiddenDanger.HiddenDangerListAdapter;
import com.xhtt.hiddendanger.Application.HiddenDangerApplication;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.InterfaceApi;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Activity.Common.FileReadActivity;
import com.xhtt.hiddendanger.UI.Activity.HiddenDanger.HiddenDangerDetailActivity;

import java.util.ArrayList;

/**
 * 隐患列表界面
 *
 * @author HG
 */

public class HiddenDangerListFragment extends BaseMVPFragment<HiddenDangerListPresenter> implements HiddenDangerListContract.View {

    private final int DIALOG_CODE_ASK_DELETE_DATA = 1000;
    private final int DIALOG_CODE_DELETE_DATA = 1001;

    private HGRefreshLayout refreshLayout;
    private View noDataView;

    private HiddenDangerListAdapter adapter;
    private ArrayList<HiddenDanger> data = new ArrayList<>();
    private Company parentData;
    private Integer status;
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private boolean isSearch = false;

    @Override
    public int bindLayout() {
        return R.layout.fragment_hidden_danger_list;
    }

    @Override
    public void initParamData() {

        status = baseUI.getParam(ParamKey.Status.getValue(), HiddenDanger.STATUS_UNCHANGED);
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
        baseUI.setSearchHint("隐患描述");
        noDataView = View.inflate(baseUI.getBaseContext(), R.layout.no_data_view_fragment_hidden_danger_list, null);
        baseUI.addNoDataView(noDataView);

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new HiddenDangerListAdapter(baseUI.getBaseContext(), R.layout.item_hidden_danger_list, data));

        refreshLayout.getRefreshLayout().autoRefresh();

        return this;
    }

    @Override
    public void setListener() {

        adapter.setOnButtonClickListener(new OnRecyclerViewItemClickListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                clickPosition = position;

                if (view.getId() == R.id.btn_edit) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
                            new Object[]{data.get(clickPosition), WorkType.Edit, parentData}
                    );
                } else if (view.getId() == R.id.btn_change) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
                            new Object[]{data.get(clickPosition), WorkType.Change, parentData}
                    );
                } else if (view.getId() == R.id.btn_detail) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
                            new Object[]{data.get(clickPosition), WorkType.Detail, parentData}
                    );
                } else if (view.getId() == R.id.cv_all) {
                    if (data.get(clickPosition).getStatus() == null) {
                        baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
                                new Object[]{data.get(clickPosition), WorkType.Edit, parentData}
                        );
                    } else if (data.get(clickPosition).getStatus() == HiddenDanger.STATUS_UNCHANGED) {
                        baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
                                new Object[]{data.get(clickPosition), WorkType.Change, parentData}
                        );
                    } else {
                        baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
                                new Object[]{data.get(clickPosition), WorkType.Detail, parentData}
                        );
                    }
                } else if (view.getId() == R.id.btn_ledger || view.getId() == R.id.btn_ledger2) {
                    // 隐患台账
                    String url = InterfaceConfig.getNowIPConfig().getRequestUrl(InterfaceApi.GetPDF.getUrl())
                            + "?token=" + MyApplication.createApplication().getToken()
                            + "&exportType=2"
                            + "&isPdf=true"
                            + "&id=" + data.get(position).getId();

                    readFile(
                            baseUI.getBaseContext(),
                            url,
                            "隐患台账"
                    );
                } else if (view.getId() == R.id.btn_changeFile || view.getId() == R.id.btn_changeFile2) {
                    // 整改文件
                    String url = InterfaceConfig.getNowIPConfig().getRequestUrl(InterfaceApi.GetPDF.getUrl())
                            + "?token=" + MyApplication.createApplication().getToken()
                            + "&exportType=3"
                            + "&isPdf=true"
                            + "&id=" + data.get(position).getId();

                    readFile(
                            baseUI.getBaseContext(),
                            url,
                            "整改文件"
                    );
                }
            }

            @Override
            public void onRecyclerViewItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                clickPosition = position;

                if (view.getId() == R.id.cv_all) {
                    HiddenDanger item = data.get(position);
                    if (item.getStatus() == null || item.getStatus() == HiddenDanger.STATUS_UNCHANGED) {
                        String sb = "确定要删除该条隐患吗？";
                        baseUI.baseDialog.showAlertDialog(R.string.tips_best, sb, DIALOG_CODE_ASK_DELETE_DATA);
                    } else {
                        t.error("该隐患已整改，无法删除");
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

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

    @Override
    public HiddenDangerListPresenter createPresenter() {
        return new HiddenDangerListPresenter();
    }

    @Override
    public void onEventUI(Event item) {

        switch (item.getEventActionCode()) {
            case EventActionCode.HIDDEN_DANGER_SUBMIT:
                HiddenDanger hiddenDanger = item.getObj(ParamKey.Company.getValue(), null);

                if (hiddenDanger != null) {
                    WorkType workType = item.getObj(ParamKey.WorkType.getValue(), null);

                    if (workType != null) {
                        switch (workType) {
                            case Add:
                            case AddFreeTake:
                                if (status == HiddenDanger.STATUS_UNCHANGED) {
                                    data.add(0, hiddenDanger);
                                    adapter.addData(data, 0, 1);
                                    new Handler().postDelayed(() -> refreshLayout.getRecyclerView().smoothScrollToPosition(0), 500);

                                    baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
                                }
                                break;
                            case Edit:
                                if (status == HiddenDanger.STATUS_UNCHANGED) {
                                    data.set(clickPosition, hiddenDanger);
                                    adapter.refreshData(data, clickPosition);
                                }
                                break;
                            case Change:
                                if (status == HiddenDanger.STATUS_UNCHANGED) {
                                    if (hiddenDanger.getStatus() == HiddenDanger.STATUS_UNCHANGED) {
                                        data.get(clickPosition).setStatus(HiddenDanger.STATUS_UNCHANGED);
                                        data.get(clickPosition).getMedia().put(1, hiddenDanger.getMedia().get(1));
                                        data.get(clickPosition).setChangeDescribe(hiddenDanger.getChangeDescribe());
                                        adapter.refreshData(data, clickPosition);
                                    } else {
                                        data.remove(clickPosition);
                                        adapter.removeData(data, clickPosition, 1);
                                    }
                                }
                                if (status == HiddenDanger.STATUS_CHANGED) {
                                    if (hiddenDanger.getStatus() == HiddenDanger.STATUS_CHANGED) {
                                        baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);

                                        hiddenDanger.setStatus(HiddenDanger.STATUS_CHANGED);
                                        data.add(0, hiddenDanger);
                                        adapter.addData(data, 0, 1);
                                        new Handler().postDelayed(() -> refreshLayout.getRecyclerView().smoothScrollToPosition(0), 500);
                                    }
                                }
                                break;
                        }
                    }
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

    private void getData() {
        mPresenter.getData(
                parentData.getCompanyName(),
                pageIndex,
                pageSize,
                searchKey,
                status,
                parentData.getCheckItemId(),
                parentData.isHiddenDangerOnce() ? parentData.getServiceId() : null
        );
    }

    @Override
    public void onSearched(String searchKey) {
        this.searchKey = searchKey.trim();
        doSearch();
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

    /**
     * 读取Office文件
     *
     * @param context  context
     * @param filepath filepath
     * @param title    title
     */
    private void readFile(Context context, String filepath, String title) {

        if (!HGSystemConfig.IS_NEED_READ_OFFICE_FILE) {
            return;
        }

        Intent intent = new Intent(context, FileReadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(HGParamKey.URL.getValue(), filepath);
        intent.putExtra(HGParamKey.Title.getValue(), title);
        context.startActivity(intent);
    }

}
