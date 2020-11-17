package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.Constant.HGParamKey;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnFloatingSearchMenuItemClickListener;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Fragment.Proxy.ProxyConfig;
import com.hg.hollowgoods.UI.Fragment.Proxy.ProxyHelper;
import com.hg.hollowgoods.Util.SearchHistory.SearchKeys;
import com.hg.hollowgoods.Widget.FloatingSearchView.suggestions.model.SearchSuggestion;
import com.hg.hollowgoods.Widget.FloatingSearchView.util.Util;
import com.hg.hollowgoods.Widget.SmartRefreshLayout.constant.RefreshState;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.widget.floatingsearchview.FloatingSearchView;
import com.hg.library_voice.VoiceListener;
import com.hg.library_voice.VoiceUtils;
import com.xhtt.hiddendanger.Adapter.HiddenDanger.CompanySelectorAdapter;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CompanyOnlyName;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 选择企业界面
 *
 * @author HG
 */

public class CompanySelectorActivity extends BaseMVPActivity<CompanySelectorPresenter> implements CompanySelectorContract.View {

    private final int DIALOG_CODE_INPUT = 1000;

    private HGRefreshLayout refreshLayout;
    private View noDataView;
    private FloatingSearchView floatingSearchView;

    private CompanySelectorAdapter adapter;
    private ArrayList<Company> data = new ArrayList<>();
    private WorkType workType;
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";
    private Class<?> fromClass;
    private boolean isSearch = false;
    private String inputKey = "";
    private String searchHint = "";
    private ArrayList<SearchKeys> keys = new ArrayList<>();
    private VoiceUtils voiceUtils;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_company_selector;
    }

    @Override
    public void initParamData() {

        workType = baseUI.getParam(ParamKey.WorkType.getValue(), WorkType.Selector);
        fromClass = baseUI.getParam(ParamKey.FromClass.getValue(), null);

        if (workType == null) {
            workType = WorkType.Selector;
        }
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_company_selector);
        if (workType == WorkType.InputOnly) {
            baseUI.setCommonRightTitleText("确定");
            baseUI.setCommonTitleText("企业名称");
        } else {
//            baseUI.setCommonRightTitleText("添加企业");
        }
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        floatingSearchView = findViewById(R.id.floating_search_view);
        initSearchView(refreshLayout);

        new Handler().postDelayed(() -> {

            refreshLayout = findViewById(R.id.hgRefreshLayout);

            noDataView = View.inflate(baseUI.getBaseContext(), R.layout.no_data_view_fragment_hidden_danger_list, null);
            if (workType == WorkType.InputOnly) {
                TextView tips = noDataView.findViewById(R.id.tv_tips);
                tips.setText("请输入企业名称并搜索");
            }
            baseUI.addNoDataView(noDataView);

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new CompanySelectorAdapter(baseUI.getBaseContext(), R.layout.item_company_selector, data));

            refreshLayout.getRefreshLayout().autoRefresh();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);

        return this;
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            refreshLayout.getRefreshLayout().setOnRefreshListener(refreshLayout -> doRefresh());

            refreshLayout.getRefreshLayout().setOnLoadMoreListener(refreshLayout -> doLoadMore());

            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener(false) {
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

            baseUI.baseDialog.setOnDialogClickListener((code, result, bundle) -> {
                if (result) {
                    switch (code) {
                        case DIALOG_CODE_INPUT:
                            inputKey = bundle.getString(HGParamKey.InputValue.getValue(), "");
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
    public void onEventUI(Event event) {
        switch (event.getEventActionCode()) {
            case EventActionCode.COMPANY_SUBMIT:
                if (event.getObj(ParamKey.WorkType.getValue(), null) == WorkType.Add) {
                    finishMyActivity();
                }
                break;
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
                    new String[]{ParamKey.ParentData.getValue(), ParamKey.WorkType.getValue(), ParamKey.FromClass.getValue()},
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

        Event event = new Event(EventActionCode.COMPANY_SELECTOR, fromClass.getName());
        event.addObj(ParamKey.StringData.getValue(), inputKey);
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

    private void backObjData() {

        Event event = new Event(EventActionCode.COMPANY_SELECTOR, fromClass.getName());
        event.addObj(ParamKey.Company.getValue(), data.get(clickPosition));
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

        if (isSearch && data.size() == 0) {
            TextView tips = noDataView.findViewById(R.id.tv_tips);
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
    public void findNameSuccess(ArrayList<Company> tempData) {

        if (tempData != null) {
            ArrayList<SearchKeys> temp = new ArrayList<>();

            for (Company t : tempData) {
                temp.add(new SearchKeys(t.getCompanyName()));
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

            floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {

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
                SearchKeys searchKeys = (SearchKeys) item;

                int textColor = getBaseContext().getResources().getColor(com.hg.hollowgoods.R.color.search_history_text);
                String textLight = getBaseContext().getString(com.hg.hollowgoods.R.string.search_history_text_light);

                if (searchKeys.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getBaseContext().getResources(),
                            com.hg.hollowgoods.R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, textColor);
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

            floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
                @Override
                public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                    // 点击了历史记录列表
                    SearchKeys searchKeys = (SearchKeys) searchSuggestion;
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

            floatingSearchView.setOnMenuItemClickListener(new OnFloatingSearchMenuItemClickListener(getBaseContext(), false) {
                @Override
                public void onFloatingSearchMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_hg_voice) {
                        if (VoiceUtils.isIncludeSDK()) {
                            if (voiceUtils == null) {
                                voiceUtils = new VoiceUtils(baseUI.getBaseContext());
                                voiceUtils.setVoiceListener(new VoiceListener() {
                                    @Override
                                    public void onStart() {
                                        floatingSearchView.showProgress();
                                    }

                                    @Override
                                    public void onEnd() {
                                        floatingSearchView.hideProgress();
                                    }

                                    @Override
                                    public void onResult(String text) {
                                        if (!floatingSearchView.isSearchBarFocused()) {
                                            floatingSearchView.setSearchFocused(true);
                                        }
                                        floatingSearchView.setSearchText(floatingSearchView.getQuery() + text);
                                    }
                                });
                            }

                            ProxyHelper.create(baseUI.getBaseContext()).requestProxy(
                                    new ProxyConfig()
                                            .setPermissions(voiceUtils.permissions)
                                            .setRequestCode(12000)
                                            .setOnProxyRequestPermissionsResult((isAgreeAll, requestCode, permissions, isAgree) -> {
                                                if (isAgreeAll) {
                                                    voiceUtils.setParam(false);
                                                    voiceUtils.start();
                                                }
                                            })
                            );
                        }
                    }
                }
            });
        }
    }

}
