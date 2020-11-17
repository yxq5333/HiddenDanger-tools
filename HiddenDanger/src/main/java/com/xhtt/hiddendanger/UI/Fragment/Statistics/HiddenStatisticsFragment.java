package com.xhtt.hiddendanger.UI.Fragment.Statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPFragment;
import com.hg.hollowgoods.Widget.RingProgressBar;
import com.hg.hollowgoods.Widget.SmartRefreshLayout.constant.RefreshState;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.widget.smartrefresh.SmartRefreshLayout;
import com.xhtt.hiddendanger.Adapter.Statistics.HiddenStatisticsAdapter;
import com.xhtt.hiddendanger.Bean.Statistics.HiddenStatistics;
import com.xhtt.hiddendanger.Bean.Statistics.HiddenStatisticsBase;
import com.xhtt.hiddendanger.Bean.Statistics.HiddenStatisticsBaseMonth;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 隐患统计界面
 *
 * @author HG
 */

public class HiddenStatisticsFragment extends BaseMVPFragment<HiddenStatisticsPresenter> implements HiddenStatisticsContract.View {

    private SmartRefreshLayout smartRefreshLayout;
    private NestedScrollView nestedScrollView;
    private CombinedChart combinedChart;
    private HGRefreshLayout refreshLayout;
    private TextView searchYear;
    private RingProgressBar progress;
    private TextView progressTips;
    private TextView hiddenDangerTotal;
    private TextView hiddenDangerChanged;
    private TextView hiddenDangerUnchanged;
    private TextView hiddenDangerSearchYearTotal1;
    private TextView hiddenDangerSearchYearTotal2;

    private HiddenStatisticsAdapter adapter;
    private ArrayList<HiddenStatistics> data = new ArrayList<>();
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int year;

    @Override
    public int bindLayout() {
        return R.layout.fragment_hidden_statistics;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        smartRefreshLayout = baseUI.findViewById(R.id.smartRefreshLayout);
        nestedScrollView = baseUI.findViewById(R.id.nestedScrollView);
        combinedChart = baseUI.findViewById(R.id.combinedChart);
        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);
        searchYear = baseUI.findViewById(R.id.tv_searchYear);
        progress = baseUI.findViewById(R.id.progress);
        progressTips = baseUI.findViewById(R.id.tv_progressTips);
        hiddenDangerTotal = baseUI.findViewById(R.id.tv_hiddenDangerTotal);
        hiddenDangerChanged = baseUI.findViewById(R.id.tv_hiddenDangerChanged);
        hiddenDangerUnchanged = baseUI.findViewById(R.id.tv_hiddenDangerUnchanged);
        hiddenDangerSearchYearTotal1 = baseUI.findViewById(R.id.tv_hiddenDangerSearchYearTotal1);
        hiddenDangerSearchYearTotal2 = baseUI.findViewById(R.id.tv_hiddenDangerSearchYearTotal2);

        smartRefreshLayout.setEnableHeaderTranslationContent(false);
        smartRefreshLayout.setEnableAutoLoadMore(false);
        smartRefreshLayout.setPrimaryColorsId(HGCommonResource.TITLE_BAR_RESOURCE, com.hg.hollowgoods.R.color.white);

        year = Calendar.getInstance().get(Calendar.YEAR);
        searchYear.setText(year + "年");

        initCombinedChart(combinedChart);

        data.add(new HiddenStatistics());

        refreshLayout.initRecyclerView();
        refreshLayout.getRecyclerView().setNestedScrollingEnabled(false);
        refreshLayout.setAdapter(adapter = new HiddenStatisticsAdapter(baseUI.getBaseContext(), R.layout.item_hidden_statistics, data));

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
    public HiddenStatisticsPresenter createPresenter() {
        return new HiddenStatisticsPresenter();
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

    private void initCombinedChart(CombinedChart chart) {

        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        chart.setNoDataText("暂无数据");

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(13f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                if (value > 0 && value < 13) {
                    return (int) value + "月";
                }

                return "";
            }
        });
    }

    private void setCombinedChartData(CombinedChart chart, ArrayList<HiddenStatisticsBaseMonth> chartData) {

        CombinedData data = new CombinedData();

        data.setData(getBarChartData(chartData));
        data.setData(getLineData(chartData));

        chart.setData(data);
        chart.invalidate();
    }

    private LineData getLineData(ArrayList<HiddenStatisticsBaseMonth> chartData) {

        LineData data = new LineData();

        // 未整改
        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 1; i <= chartData.size(); i++) {
            values1.add(new Entry(i, chartData.get(i - 1).getHiddenDangerUnchanged()));
        }

        LineDataSet set1 = new LineDataSet(values1, "未整改");
        set1.setColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_line_chart_color_unchanged));
        set1.setLineWidth(1.5f);
        set1.setCircleColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_line_chart_color_unchanged));
        set1.setCircleRadius(3f);
        set1.setFillColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_line_chart_color_unchanged));
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawValues(true);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_line_chart_color_unchanged));
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        // 已整改
        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 1; i <= chartData.size(); i++) {
            values2.add(new Entry(i, chartData.get(i - 1).getHiddenDangerChanged()));
        }

        LineDataSet set2 = new LineDataSet(values2, "已整改");
        set2.setColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_line_chart_color_changed));
        set2.setLineWidth(1.5f);
        set2.setCircleColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_line_chart_color_changed));
        set2.setCircleRadius(3f);
        set2.setFillColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_line_chart_color_changed));
        set2.setMode(LineDataSet.Mode.LINEAR);
        set2.setDrawValues(true);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_line_chart_color_changed));
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        data.addDataSet(set1);
        data.addDataSet(set2);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + "";
            }
        });

        return data;
    }

    private BarData getBarChartData(ArrayList<HiddenStatisticsBaseMonth> chartData) {

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 1; i <= chartData.size(); i++) {
            values.add(new BarEntry(i, chartData.get(i - 1).getHiddenDangerTotal()));
        }

        BarDataSet set1;
        set1 = new BarDataSet(values, "隐患总数");
        set1.setDrawIcons(false);

        int startColor = ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_bar_chart_color_start);
        int endColor = ContextCompat.getColor(baseUI.getBaseContext(), R.color.statistics_hidden_bar_chart_color_end);
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

        return data;
    }

    public void toTop() {
        if (nestedScrollView != null) {
            nestedScrollView.fling(0);
            nestedScrollView.smoothScrollTo(0, 0);
        }
    }

    @Override
    public void getBaseDataSuccess(HiddenStatisticsBase hiddenStatisticsBase) {

        baseUI.getBaseContext().runOnUiThread(() -> {
            if (hiddenStatisticsBase.getHiddenDangerTotal() == 0) {
                progress.setProgress(0);
            } else {
                progress.setProgress(hiddenStatisticsBase.getHiddenDangerChanged() * 100 / hiddenStatisticsBase.getHiddenDangerTotal());
            }
            progressTips.setText("已整改\n"
                    + hiddenStatisticsBase.getHiddenDangerChanged()
                    + "/"
                    + hiddenStatisticsBase.getHiddenDangerTotal()
            );
            hiddenDangerTotal.setText("隐患总数:" + hiddenStatisticsBase.getHiddenDangerTotal());
            hiddenDangerChanged.setText("已整改:" + hiddenStatisticsBase.getHiddenDangerChanged() + "");
            hiddenDangerUnchanged.setText("未整改:" + hiddenStatisticsBase.getHiddenDangerUnchanged() + "");
            hiddenDangerSearchYearTotal1.setText("隐患总数(" + hiddenStatisticsBase.getSearchYearHiddenDangerCount() + ")");
            hiddenDangerSearchYearTotal2.setText("隐患总数(" + hiddenStatisticsBase.getSearchYearHiddenDangerCount() + ")");

            setCombinedChartData(combinedChart, hiddenStatisticsBase.getList());
        });
    }

    @Override
    public void getBaseDataError() {

    }

    @Override
    public void getBaseDataFinish() {

    }

    @Override
    public void getListDataSuccess(ArrayList<HiddenStatistics> tempData) {

        if (!isLoadMore) {
            data.clear();
            data.add(new HiddenStatistics());
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
