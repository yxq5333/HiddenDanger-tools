package com.xhtt.hiddendangermaster.adapter.knowledgebase.msds;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDS;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public class MSDSAdapter extends CommonAdapter<MSDS> {

    public MSDSAdapter(Context context, int layoutId, List<MSDS> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, MSDS item, int position) {
        viewHolder.setText(R.id.tv_title, item.getChemicalsNameCn());
    }

}
