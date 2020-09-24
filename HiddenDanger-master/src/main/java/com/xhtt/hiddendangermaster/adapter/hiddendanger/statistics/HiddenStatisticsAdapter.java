package com.xhtt.hiddendangermaster.adapter.hiddendanger.statistics;

import android.content.Context;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.statistics.HiddenStatistics;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-10.
 */
public class HiddenStatisticsAdapter extends CommonAdapter<HiddenStatistics> {

    public HiddenStatisticsAdapter(Context context, int layoutId, List<HiddenStatistics> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, HiddenStatistics item, int position) {

        if (position == 0) {
            viewHolder.setText(R.id.tv_number, "序号");
            viewHolder.setText(R.id.tv_companyName, "企业名称");
            viewHolder.setText(R.id.tv_total, "隐患数");
            viewHolder.setText(R.id.tv_changedCount, "已整改");
            viewHolder.setText(R.id.tv_unchangedCount, "未整改");
            viewHolder.setText(R.id.tv_changeProgress, "整改进度");

            viewHolder.setTextColorRes(R.id.tv_changeProgress, R.color.txt_color_dark);
            viewHolder.setVisible(R.id.pro_changeProgress, false);
        } else {
            viewHolder.setText(R.id.tv_number, position + "");
            viewHolder.setText(R.id.tv_companyName, item.getCompanyName());
            viewHolder.setText(R.id.tv_total, item.getHiddenDangerTotal() + "");
            viewHolder.setText(R.id.tv_changedCount, item.getHiddenDangerChanged() + "");
            viewHolder.setText(R.id.tv_unchangedCount, item.getHiddenDangerUnchanged() + "");

            RoundCornerProgressBar progressBar = viewHolder.getView(R.id.pro_changeProgress);

            if (item.getHiddenDangerTotal() == 0) {
                viewHolder.setText(R.id.tv_changeProgress, "0%");
                progressBar.setProgress(0);
            } else {
                viewHolder.setText(R.id.tv_changeProgress, (item.getHiddenDangerChanged() * 100 / item.getHiddenDangerTotal()) + "%");
                progressBar.setProgress(item.getHiddenDangerChanged() * 100F / item.getHiddenDangerTotal());
            }

            viewHolder.setTextColorRes(R.id.tv_changeProgress, R.color.white);
            viewHolder.setVisible(R.id.pro_changeProgress, true);
        }

        viewHolder.setBackgroundRes(R.id.ll_bg, position % 2 == 0 ? R.color.statistics_hidden_item_bg : R.color.white);
    }

}
