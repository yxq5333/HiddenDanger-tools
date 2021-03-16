package com.xhtt.hiddendangermaster.adapter.knowledgebase.msds;

import android.content.Context;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDS;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public class MSDSAdapter extends ZCommonAdapter<MSDS> {

    public MSDSAdapter(Context context, int layoutId, List<MSDS> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, MSDS item, int position) {
        viewHolder.setText(R.id.tv_title, item.getChemicalsNameCn());
    }

}
