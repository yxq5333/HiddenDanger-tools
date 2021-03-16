package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.accidentcase;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.zero.anim.recyclerview.adapters.ZScaleInAnimationAdapter;
import com.hg.zero.anim.recyclerview.animators.ZLandingAnimator;
import com.hg.zero.constant.ZParamKey;
import com.hg.zero.datetime.ZDateTimeUtils;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.widget.runtextview.ZRunTextViewVertical;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.knowledgebase.accidentcase.AccidentCaseAdapter;
import com.xhtt.hiddendangermaster.bean.knowledgebase.accidentcase.AccidentCase;
import com.xhtt.hiddendangermaster.bean.knowledgebase.common.FileDetail;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.common.FileDetailActivity;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPFragment;

import java.util.ArrayList;

/**
 * 事故案例碎片
 *
 * @author HG
 */

public class AccidentCaseFragment extends HDBaseMVPFragment<AccidentCasePresenter> implements AccidentCaseContract.View {

    private ZRunTextViewVertical runText;
    private RecyclerView result;

    private AccidentCaseAdapter adapter;
    private ArrayList<AccidentCase> data = new ArrayList<>();
    private ArrayList<AccidentCase> hotData = new ArrayList<>();
    private ArrayList<String> runData = new ArrayList<>();
    private AccidentCaseContract.View listenerView;
    private int clickPosition;
    private boolean isLoadMore = false;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String searchKey = "";

    @Override
    public int bindLayout() {
        return R.layout.fragment_accident_case;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        runText = baseUI.findViewById(R.id.runText);
        result = baseUI.findViewById(R.id.rv_result);

        //设置属性
        runText.setText(16, 0, ContextCompat.getColor(baseUI.getBaseContext(), R.color.txt_color_dark));
        //设置停留时长间隔
        runText.setTextStillTime(3000);
        //设置进入和退出的时间间隔
        runText.setAnimTime(300);
        runText.setOnItemClickListener(position -> {

            mPresenter.addReadCount(hotData.get(position).getId());

            FileDetail fileDetail = new FileDetail();
            fileDetail.setActivityTitle("详情");
            fileDetail.setTitle(hotData.get(position).getTitle());
            fileDetail.setContent(hotData.get(position).getContent());
            fileDetail.setReleaseTime(ZDateTimeUtils.getDateTimeString(hotData.get(position).getDate(), ZDateTimeUtils.DateFormatMode.LINE_YMDHM));

            baseUI.startMyActivity(FileDetailActivity.class,
                    new Enum[]{ZParamKey.AppFiles},
                    new Object[]{fileDetail}
            );
        });

        result.setHasFixedSize(true);
        result.setItemAnimator(new ZLandingAnimator());
        result.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));
        result.setNestedScrollingEnabled(false);

        adapter = new AccidentCaseAdapter(baseUI.getBaseContext(), R.layout.item_accident_case, data);
        result.setAdapter(new ZScaleInAnimationAdapter(adapter));

        doRefresh();
    }

    @Override
    public void setListener() {

        adapter.setOnItemClickListener(new ZOnRecyclerViewItemClickOldListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                mPresenter.addReadCount(data.get(position).getId());

                FileDetail fileDetail = new FileDetail();
                fileDetail.setActivityTitle("详情");
                fileDetail.setTitle(data.get(position).getTitle());
                fileDetail.setContent(data.get(position).getContent());
                fileDetail.setReleaseTime(ZDateTimeUtils.getDateTimeString(data.get(position).getDate(), ZDateTimeUtils.DateFormatMode.LINE_YMDHM));

                baseUI.startMyActivity(FileDetailActivity.class,
                        new Enum[]{ZParamKey.AppFiles},
                        new Object[]{fileDetail}
                );
            }
        });
    }

    @Override
    public AccidentCasePresenter createPresenter() {
        return new AccidentCasePresenter();
    }

    public void doLoadMore() {
        isLoadMore = true;
        pageIndex++;
        getData();
    }

    public void doRefresh() {
        isLoadMore = false;
        pageIndex = 1;
        getData();
        mPresenter.getHotData(pageIndex, pageSize, searchKey);
    }

    @Override
    public void onSearched(String searchKey) {
        this.searchKey = searchKey.trim();
        doRefresh();
    }

    private void getData() {
        mPresenter.getData(pageIndex, pageSize, searchKey);
    }

    @Override
    public void getDataSuccess(ArrayList<AccidentCase> tempData) {

        if (!isLoadMore) {
            data.clear();
        }

        if (tempData != null) {
            data.addAll(tempData);

            if (tempData.size() < pageSize) {
                // 没有更多数据
                listenerView.getDataNoMore();
            }
        } else {
            // 没有更多数据
            listenerView.getDataNoMore();
        }

        baseUI.getBaseContext().runOnUiThread(() -> {
            adapter.refreshData(data);
            getDataFinish();
        });
    }

    @Override
    public void getDataError() {
        // 没有更多数据
        listenerView.getDataNoMore();
    }

    @Override
    public void getDataFinish() {
        if (listenerView != null) {
            listenerView.getDataFinish();
        }
    }

    @Override
    public void getHotDataSuccess(ArrayList<AccidentCase> tempData) {

        hotData.clear();
        runData.clear();

        if (tempData != null) {
            hotData.addAll(tempData);

            for (AccidentCase t : hotData) {
                runData.add(t.getTitle());
            }
        }

        baseUI.getBaseContext().runOnUiThread(() -> {
            runText.clearAllText();
            runText.addTextList(runData);
        });
    }

    @Override
    public void getHotDataError() {

    }

    @Override
    public void getHotDataFinish() {

    }

    public void setListenerView(AccidentCaseContract.View listenerView) {
        this.listenerView = listenerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        runText.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        runText.stopAutoScroll();
    }

}
