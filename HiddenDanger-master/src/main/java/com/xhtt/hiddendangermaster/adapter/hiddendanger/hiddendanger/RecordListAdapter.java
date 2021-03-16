package com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger;

import android.content.Context;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Record;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class RecordListAdapter extends ZCommonAdapter<Record> {

    public RecordListAdapter(Context context, int layoutId, List<Record> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, Record item, int position) {

        viewHolder.setText(R.id.tv_checkDate, item.getCheckDate());
        viewHolder.setText(R.id.tv_count, "第" + item.getTimes() + "次检查");
        viewHolder.setText(R.id.tv_progress, "整改进度" + item.getHiddenDangerChangedCount() + "/" + item.getHiddenDangerTotal());
        viewHolder.setText(R.id.tv_total, "隐患总数 " + item.getHiddenDangerTotal());
        viewHolder.setText(R.id.tv_changedCount, " " + item.getHiddenDangerChangedCount());
        viewHolder.setText(R.id.tv_unchangedCount, " " + item.getHiddenDangerUnchangedCount());
    }

}
