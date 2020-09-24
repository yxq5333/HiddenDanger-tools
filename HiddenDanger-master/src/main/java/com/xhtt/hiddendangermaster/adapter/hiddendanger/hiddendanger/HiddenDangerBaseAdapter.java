package com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class HiddenDangerBaseAdapter extends CommonAdapter<HiddenDanger> {

    public HiddenDangerBaseAdapter(Context context, int layoutId, List<HiddenDanger> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, HiddenDanger item, int position) {

        viewHolder.setText(R.id.tv_hiddenDangerDescribe, item.getHiddenDescribe());
        viewHolder.setText(R.id.tv_reference, item.getReference());
        viewHolder.setText(R.id.tv_changeFunction, item.getChangeFunction());
    }

}
