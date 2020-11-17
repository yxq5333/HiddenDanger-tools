package com.xhtt.hiddendanger.Adapter.HiddenDanger;

import android.content.Context;

import com.hg.hollowgoods.Adapter.BaseRecyclerView.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.R;

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
