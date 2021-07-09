package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZSystemConfig;
import com.hg.zero.constant.ZParamKey;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger.HiddenDangerListAdapter;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.FileReadActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.HiddenDangerDetailActivity;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPFragment;

import java.util.ArrayList;

/**
 * 隐患列表界面
 *
 * @author HG
 */

public class HiddenDangerListFragment extends HDBaseMVPFragment<HiddenDangerListPresenter> implements HiddenDangerListContract.View {

    private final int DIALOG_CODE_ASK_DELETE_DATA = 1000;
    private final int DIALOG_CODE_DELETE_DATA = 1001;

    private ZRefreshLayout refreshLayout;

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
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_hidden_danger_list;
    }

    @Override
    public void initParamData() {

        super.initParamData();
        status = baseUI.getParam(ParamKey.Status, HiddenDanger.STATUS_UNCHANGED);
        parentData = baseUI.getParam(ParamKey.ParentData, null);

        if (parentData == null) {
            parentData = new Company();
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.ZRefreshLayout);

        baseUI.initSearchView(refreshLayout, true);
        baseUI.setSearchHint("隐患描述");

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new HiddenDangerListAdapter(baseUI.getBaseContext(), R.layout.item_hidden_danger_list, data));

        refreshLayout.getRefreshLayout().autoRefresh();
    }

    @Override
    public void setListener() {

        adapter.setOnButtonClickListener(new ZOnRecyclerViewItemClickOldListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                clickPosition = position;

                if (view.getId() == R.id.btn_edit) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.GrandData},
                            new Object[]{data.get(clickPosition), WorkType.Edit, parentData}
                    );
                } else if (view.getId() == R.id.btn_change) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.GrandData},
                            new Object[]{data.get(clickPosition), WorkType.Change, parentData}
                    );
                } else if (view.getId() == R.id.btn_detail) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.GrandData},
                            new Object[]{data.get(clickPosition), WorkType.Detail, parentData}
                    );
                } else if (view.getId() == R.id.cv_all) {
                    if (data.get(clickPosition).getStatus() == null) {
                        baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.GrandData},
                                new Object[]{data.get(clickPosition), WorkType.Edit, parentData}
                        );
                    } else if (data.get(clickPosition).getStatus() == HiddenDanger.STATUS_UNCHANGED) {
                        baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.GrandData},
                                new Object[]{data.get(clickPosition), WorkType.Change, parentData}
                        );
                    } else {
                        baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.GrandData},
                                new Object[]{data.get(clickPosition), WorkType.Detail, parentData}
                        );
                    }
                } else if (view.getId() == R.id.btn_ledger || view.getId() == R.id.btn_ledger2) {
                    // 隐患台账
                    String url = ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.GetPDF.getUrl())
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
                    String url = ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.GetPDF.getUrl())
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
                        baseUI.baseDialog.showAlertDialog(new ZDialogConfig.AlertConfig(DIALOG_CODE_ASK_DELETE_DATA)
                                .setTitle(R.string.z_tips_best)
                                .setContent(sb)
                        );
                    } else {
                        Zt.error("该隐患已整改，无法删除");
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        refreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());

        baseUI.baseDialog.addOnDialogClickListener((code, result, backData) -> {

            if (result) {
                switch (code) {
                    case DIALOG_CODE_ASK_DELETE_DATA:
                        baseUI.baseDialog.showProgressDialog(new ZDialogConfig.ProgressConfig(DIALOG_CODE_DELETE_DATA)
                                .setContent("删除中，请稍候……")
                        );
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
    public void onEventUI(ZEvent item) {
        if (item.getEventActionCode() == EventActionCode.HIDDEN_DANGER_SUBMIT) {
            doRefresh();
//            HiddenDanger hiddenDanger = item.getObj(ParamKey.Company, null);

//            if (hiddenDanger != null) {
//                WorkType workType = item.getObj(ParamKey.WorkType, null);
//
//                if (workType != null) {
//                    switch (workType) {
//                        case Add:
//                        case AddFreeTake:
//                            if (status == HiddenDanger.STATUS_UNCHANGED) {
//                                data.add(0, hiddenDanger);
//                                adapter.addData(data, 0, 1);
//                                new Handler().postDelayed(() -> refreshLayout.getRecyclerView().smoothScrollToPosition(0), 500);
//
//                                baseUI.setStatus(ZStatusLayout.Status.Default);
//                            }
//                            break;
//                        case Edit:
//                            if (status == HiddenDanger.STATUS_UNCHANGED) {
//                                data.set(clickPosition, hiddenDanger);
//                                adapter.refreshData(data, clickPosition);
//                            }
//                            break;
//                        case Change:
//                            if (status == HiddenDanger.STATUS_UNCHANGED) {
//                                if (hiddenDanger.getStatus() == HiddenDanger.STATUS_UNCHANGED) {
//                                    data.get(clickPosition).setStatus(HiddenDanger.STATUS_UNCHANGED);
//                                    data.get(clickPosition).setAppChangePhotoList(hiddenDanger.getAppChangePhotoList());
//                                    data.get(clickPosition).setChangeDescribe(hiddenDanger.getChangeDescribe());
//                                    adapter.refreshData(data, clickPosition);
//                                } else {
//                                    data.remove(clickPosition);
//                                    adapter.removeData(data, clickPosition, 1);
//                                }
//                            }
//                            if (status == HiddenDanger.STATUS_CHANGED) {
//                                if (hiddenDanger.getStatus() == HiddenDanger.STATUS_CHANGED) {
//                                    baseUI.setStatus(ZStatusLayout.Status.Default);
//
//                                    hiddenDanger.setStatus(HiddenDanger.STATUS_CHANGED);
//                                    data.add(0, hiddenDanger);
//                                    adapter.addData(data, 0, 1);
//                                    new Handler().postDelayed(() -> refreshLayout.getRecyclerView().smoothScrollToPosition(0), 500);
//                                }
//                            }
//                            break;
//                    }
//                }
//            }
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

        if (data.size() > 0) {
            baseUI.setStatus(ZStatusLayout.Status.Default);
        } else {
            baseUI.setStatus(ZStatusLayout.Status.NoData);
        }

        if (isSearch && data.size() == 0) {
            TextView tips = baseUI.findViewById(R.id.tv_tips);
            tips.setText("未找到相关数据");
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

    @Override
    public void deleteDataSuccess() {
        Zt.success("删除成功");
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

        if (!ZSystemConfig.isOpenOfficeFileReader()) {
            return;
        }

        Intent intent = new Intent(context, FileReadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(ZParamKey.URL.toString(), filepath);
        intent.putExtra(ZParamKey.Title.toString(), title);
        context.startActivity(intent);
    }

}
