package com.xhtt.hiddendanger.UI.Fragment.Statistics;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPFragment;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.widget.smartrefresh.SmartRefreshLayout;
import com.hg.hollowgoods.Widget.SmartRefreshLayout.constant.RefreshState;
import com.xhtt.hiddendanger.Adapter.Statistics.ServiceCompanyAdapter;
import com.xhtt.hiddendanger.Bean.Statistics.ServiceCompany;
import com.xhtt.hiddendanger.Bean.Statistics.ServiceCompanyBase;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 服务企业界面
 *
 * @author HG
 */

public class ServiceCompanyFragment extends BaseMVPFragment<ServiceCompanyPresenter> implements ServiceCompanyContract.View {

    private SmartRefreshLayout smartRefreshLayout;
    private BarChart barChart;
    private HGRefreshLayout refreshLayout;
    private NestedScrollView nestedScrollView;
    private TextView searchYear;
    private TextView total;
    private TextView nowYearCount;
    private TextView searchYearCount1;
    private TextView searchYearCount2;

    private ServiceCompanyAdapter adapter;
    private ArrayList<ServiceCompany> data = new ArrayList<>();
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int year;

    @Override
    public int bindLayout() {
        return R.layout.fragment_service_company;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        smartRefreshLayout = baseUI.findViewById(R.id.smartRefreshLayout);
        barChart = baseUI.findViewById(R.id.barChart);
        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);
        nestedScrollView = baseUI.findViewById(R.id.nestedScrollView);
        searchYear = baseUI.findViewById(R.id.tv_searchYear);
        total = baseUI.findViewById(R.id.tv_total);
        nowYearCount = baseUI.findViewById(R.id.tv_nowYearCount);
        searchYearCount1 = baseUI.findViewById(R.id.tv_searchYearCount1);
        searchYearCount2 = baseUI.findViewById(R.id.tv_searchYearCount2);

        smartRefreshLayout.setEnableHeaderTranslationContent(false);
        smartRefreshLayout.setEnableAutoLoadMore(false);
        smartRefreshLayout.setPrimaryColorsId(HGCommonResource.TITLE_BAR_RESOURCE, com.hg.hollowgoods.R.color.white);

        year = Calendar.getInstance().get(Calendar.YEAR);
        searchYear.setText(year + "年");

        initBarChart(barChart);

        data.add(new ServiceCompany());

        refreshLayout.initRecyclerView();
        refreshLayout.getRecyclerView().setNestedScrollingEnabled(false);
        refreshLayout.setAdapter(adapter = new ServiceCompanyAdapter(baseUI.getBaseContext(), R.layout.item_service_company, data));

        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void setListener() {

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> doLoadMore());
    }

    @Override
    public ServiceCompanyPresenter createPresenter() {
        return new ServiceCompanyPresenter();
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
        mPresenter.getBaseData(year);
    }

    private void getData() {
        mPresenter.getListData(pageIndex, pageSize, year);
    }

    private void initBarChart(BarChart chart) {

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);
        chart.setNoDataText("暂无数据");

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return (int) value + "月";
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);
    }

    private void setBarChartData(BarChart chart, ArrayList<Integer> chartData) {

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 1; i <= chartData.size(); i++) {
            values.add(new BarEntry(i, chartData.get(i - 1)));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);

            int startColor = ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_service_company_bar_chart_color_start);
            int endColor = ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_service_company_bar_chart_color_end);
            set1.setGradientColor(startColor, endColor);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.5f);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return (int) value + "";
                }
            });

            chart.setData(data);
        }

        chart.invalidate();
    }

    public void toTop() {
        if (nestedScrollView != null) {
            nestedScrollView.fling(0);
            nestedScrollView.smoothScrollTo(0, 0);
        }
    }

    @Override
    public void getBaseDataSuccess(ServiceCompanyBase serviceCompanyBase) {

        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < serviceCompanyBase.getList().size(); i++) {
            temp.add(serviceCompanyBase.getList().get(i).get((i + 1) + ""));
        }

        baseUI.getBaseContext().runOnUiThread(() -> {
            total.setText(serviceCompanyBase.getTotalCnt() + "");
            nowYearCount.setText(serviceCompanyBase.getYearCompanyCnt() + "");
            searchYearCount1.setText("检查企业(" + serviceCompanyBase.getCurrentCnt() + "家)");
            searchYearCount2.setText("检查企业(" + serviceCompanyBase.getCurrentCnt() + "家)");
            setBarChartData(barChart, temp);
        });
    }

    @Override
    public void getBaseDataError() {

    }

    @Override
    public void getBaseDataFinish() {

    }

    @Override
    public void getListDataSuccess(ArrayList<ServiceCompany> tempData) {

        if (!isLoadMore) {
            data.clear();
            data.add(new ServiceCompany());
        }

        if (tempData != null) {
            data.addAll(tempData);

            if (tempData.size() < pageSize) {
                baseUI.getBaseContext().runOnUiThread(() -> smartRefreshLayout.setNoMoreData(true));
            }
        } else {
            baseUI.getBaseContext().runOnUiThread(() -> smartRefreshLayout.setNoMoreData(true));
        }

        baseUI.getBaseContext().runOnUiThread(() -> {
            adapter.refreshData(data);
            getListDataFinish();
        });
    }

    @Override
    public void getListDataError() {
        smartRefreshLayout.setNoMoreData(true);
    }

    @Override
    public void getListDataFinish() {
        new Handler().postDelayed(() -> {

            if (smartRefreshLayout.getState() == RefreshState.Refreshing || smartRefreshLayout.getState() == RefreshState.RefreshReleased) {
                smartRefreshLayout.finishRefresh();
            }

            if (smartRefreshLayout.getState() == RefreshState.Loading) {
                if (smartRefreshLayout.isNoMoreData()) {
                    smartRefreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    smartRefreshLayout.finishLoadMore();
                }
            }
        }, SystemConfig.DELAY_TIME_REFRESH_DATA);
    }
}
