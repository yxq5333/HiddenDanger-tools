package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.constant.ZParamKey;
import com.hg.zero.listener.ZOnFloatingSearchMenuItemClickListener;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.util.ZResUtils;
import com.hg.zero.util.searchhistory.ZSearchKeys;
import com.hg.zero.widget.floatingsearchview.ZFloatingSearchView;
import com.hg.zero.widget.floatingsearchview.suggestions.model.ZSearchSuggestion;
import com.hg.zero.widget.floatingsearchview.util.ZFloatingSearchViewUtil;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger.CompanySelectorAdapter;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CompanyOnlyName;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 选择企业界面
 *
 * @author HG
 */

public class CompanySelectorActivity extends HDBaseMVPActivity<CompanySelectorPresenter> implements CompanySelectorContract.View {

    private final int DIALOG_CODE_INPUT = 1000;

    private ZRefreshLayout refreshLayout;
    private ZFloatingSearchView floatingSearchView;

    private CompanySelectorAdapter adapter;
    private ArrayList<Company> data = new ArrayList<>();
    private WorkType workType;
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private boolean isSearch = false;
    private String inputKey = "";
    private String searchHint = "";
    private ArrayList<ZSearchKeys> keys = new ArrayList<>();

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_company_selector;
    }

    @Override
    public void initParamData() {

        super.initParamData();
        workType = baseUI.getParam(ParamKey.WorkType, WorkType.Selector);

        if (workType == null) {
            workType = WorkType.Selector;
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_company_selector);
        if (workType == WorkType.InputOnly) {
            baseUI.setCommonRightTitleText("确定");
            baseUI.setCommonTitleText("企业名称");
        } else {
//            baseUI.setCommonRightTitleText("添加企业");
        }
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        floatingSearchView = findViewById(R.id.floating_search_view);
        initSearchView(refreshLayout);

        new Handler().postDelayed(() -> {

            refreshLayout = findViewById(R.id.ZRefreshLayout);

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new CompanySelectorAdapter(baseUI.getBaseContext(), R.layout.item_company_selector, data));

            refreshLayout.getRefreshLayout().autoRefresh();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            refreshLayout.getRefreshLayout().setOnRefreshListener(refreshLayout -> doRefresh());

            refreshLayout.getRefreshLayout().setOnLoadMoreListener(refreshLayout -> doLoadMore());

            adapter.setOnItemClickListener(new ZOnRecyclerViewItemClickOldListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    clickPosition = position;

//                    if (workType == WorkType.InputOnly) {
//                        baseUI.startMyActivity(HiddenDangerListActivity.class,
//                                new String[]{ParamKey.ParentData.getValue()},
//                                new Object[]{data.get(clickPosition)}
//                        );
//                    } else {
                    backObjData();
//                    }
                }
            });

            baseUI.baseDialog.addOnDialogClickListener((code, result, bundle) -> {
                if (result) {
                    switch (code) {
                        case DIALOG_CODE_INPUT:
                            inputKey = bundle.getData(ZParamKey.InputValue, "");
                            backStringData();
                            break;
                    }
                }
            });
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public CompanySelectorPresenter createPresenter() {
        return new CompanySelectorPresenter();
    }

    @Override
    public void onEventUI(ZEvent event) {
        if (event.getEventActionCode() == EventActionCode.COMPANY_SUBMIT) {
            if (event.getObj(ParamKey.WorkType, null) == WorkType.Add) {
                finishMyActivity();
            }
        }
    }

    @Override
    public void onRightTitleClick(View view, int id) {

        if (workType == WorkType.InputOnly) {
            backStringData();
        } else {
            CompanyOnlyName company = new CompanyOnlyName();
            company.setCompanyName(TextUtils.isEmpty(inputKey) ? searchKey : inputKey);
            company.setShowOther(false);
            baseUI.startMyActivity(CompanyDetailOnlyNameActivity.class,
                    new Enum[]{ParamKey.ParentData, ParamKey.WorkType, ParamKey.FromClass},
                    new Object[]{company, WorkType.Add, this.getClass()}
            );
//            ConfigInput configInput = new ConfigInput()
//                    .setTitle("请输入新增的企业名称")
//                    .setText(TextUtils.isEmpty(inputKey) ? searchKey : inputKey)
//                    .setValidator(ValidatorFactory.getValidator(ValidatorType.MIN_LENGTH, "至少输入1个字符", 1));
//
//            baseUI.baseDialog.showInputDialog(configInput, DIALOG_CODE_INPUT);
        }
    }

    private void backStringData() {

        ZEvent event = baseUI.buildEvent(EventActionCode.COMPANY_SELECTOR);
        event.addObj(ParamKey.StringData, inputKey);
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

    private void backObjData() {

        ZEvent event = baseUI.buildEvent(EventActionCode.COMPANY_SELECTOR);
        event.addObj(ParamKey.Company, data.get(clickPosition));
        EventBus.getDefault().post(event);

        finishMyActivity();
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
        mPresenter.getData(pageIndex, pageSize, workType == WorkType.InputOnly ? (TextUtils.isEmpty(searchKey) ? Constants.DEFAULT_SEARCH_KEY : searchKey) : searchKey);
    }

    @Override
    public void getDataSuccess(ArrayList<Company> tempData) {

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

        runOnUiThread(() -> {
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
            if (workType == WorkType.InputOnly) {
                if (TextUtils.isEmpty(searchKey)) {
                    tips.setText("请输入企业名称并搜索");
                } else {
                    tips.setText("未找到相关数据");
                }
            } else {
                tips.setText("未找到相关数据");
            }
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
    public void findNameSuccess(ArrayList<Company> tempData) {

        if (tempData != null) {
            ArrayList<ZSearchKeys> temp = new ArrayList<>();

            for (Company t : tempData) {
                temp.add(new ZSearchKeys(t.getCompanyName()));
            }

            keys.clear();
            keys.addAll(temp);

            runOnUiThread(() -> {
                floatingSearchView.swapSuggestions(keys);
                findNameFinish();
            });
        } else {
            runOnUiThread(() -> {
                findNameError();
                findNameFinish();
            });
        }
    }

    @Override
    public void findNameError() {
        keys.clear();
        floatingSearchView.swapSuggestions(keys);
    }

    @Override
    public void findNameFinish() {
        floatingSearchView.hideProgress();
    }

    private void initSearchView(View dataView) {

        if (floatingSearchView != null) {
            floatingSearchView.setVisibility(View.VISIBLE);
            floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {

                // 输入监听
                // 输入框无内容则清空历史记录列表
                // 否则进行匹配
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    floatingSearchView.clearSuggestions();
                } else {
                    inputKey = newQuery;
                    if (TextUtils.isEmpty(inputKey)) {
                        floatingSearchView.hideProgress();
                        keys.clear();
                        floatingSearchView.swapSuggestions(keys);
                    } else {
                        floatingSearchView.showProgress();
                        mPresenter.findName(inputKey);
                    }
                }
            });

            floatingSearchView.setOnFocusChangeListener(new ZFloatingSearchView.OnFocusChangeListener() {

                @Override
                public void onFocus() {

                    // 获取到焦点
                    floatingSearchView.setSearchBarTitle(TextUtils.isEmpty(searchKey) ? "" : searchKey);
                    floatingSearchView.setSearchHint("");
                    floatingSearchView.swapSuggestions(keys);
                }

                @Override
                public void onFocusCleared() {

                    // 焦点消失
                    floatingSearchView.setSearchBarTitle("");
                    floatingSearchView.setSearchHint(TextUtils.isEmpty(searchKey) ? searchHint : searchKey);
                }
            });

            floatingSearchView.setOnBindSuggestionCallback((suggestionView, leftIcon, textView, item, itemPosition) -> {
                // 历史列表绑定数据
                ZSearchKeys searchKeys = (ZSearchKeys) item;

                int textColor = getBaseContext().getResources().getColor(R.color.search_history_text);
                String textLight = ZResUtils.getColorValueById(getBaseContext(), R.color.colorAccent, false);

                if (searchKeys.isHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getBaseContext().getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    ZFloatingSearchViewUtil.setIconColor(leftIcon, textColor);
                    leftIcon.setAlpha(0.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(textColor);
                String text = searchKeys.getBody()
                        .replaceFirst(floatingSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + floatingSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            });

            floatingSearchView.setOnSearchListener(new ZFloatingSearchView.OnSearchListener() {
                @Override
                public void onSuggestionClicked(final ZSearchSuggestion searchSuggestion) {

                    // 点击了历史记录列表
                    ZSearchKeys searchKeys = (ZSearchKeys) searchSuggestion;
                    searchKey = searchKeys.getBody();
                    inputKey = searchKey;
                    doSearch();
                    floatingSearchView.clearSearchFocus();
                }

                @Override
                public void onSearchAction(String query) {
                    searchKey = query;
                    inputKey = searchKey;
                    doSearch();
                }
            });

            floatingSearchView.setOnSuggestionsListHeightChanged(newHeight -> {

                // 历史记录列表长度变化
                // 结果列表位置也需变化
                if (dataView != null) {
                    dataView.setTranslationY(newHeight);
                }
            });

            floatingSearchView.setOnArrowActionClickListener(() -> {
                searchKey = "";
                inputKey = searchKey;
                doSearch();
                floatingSearchView.clearSearchFocus();
            });

            floatingSearchView.setOnMenuItemClickListener(new ZOnFloatingSearchMenuItemClickListener(getBaseContext(), false) {
                @Override
                public void onFloatingSearchMenuItemClick(MenuItem item) {

                }
            });
        }
    }

}
