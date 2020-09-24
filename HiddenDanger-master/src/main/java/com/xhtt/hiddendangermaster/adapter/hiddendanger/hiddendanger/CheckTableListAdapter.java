package com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CheckTable;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class CheckTableListAdapter extends CommonAdapter<CheckTable> {

    public CheckTableListAdapter(Context context, int layoutId, List<CheckTable> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, CheckTable item, int position) {

        viewHolder.setText(R.id.tv_checkDate, item.getCheckDate());
        viewHolder.setText(R.id.tv_checkTableName, item.getCheckTableName());

        viewHolder.setVisible(R.id.ll_checkDate, item.getStatus() == CheckTable.STATUS_CHECKED);

        switch (item.getStatus()) {
            case CheckTable.STATUS_UNCHECKED:
                viewHolder.setSlanted(R.id.status, R.color.grey2, "未检查");
                break;
            case CheckTable.STATUS_CHECK_ING:
                viewHolder.setSlanted(R.id.status, R.color.google_green, "检查中");
                break;
            case CheckTable.STATUS_CHECKED:
                viewHolder.setSlanted(R.id.status, R.color.colorAccent, "已检查");
                break;
            default:
                viewHolder.setSlanted(R.id.status, R.color.google_red, "未知");
                break;
        }
    }

}
