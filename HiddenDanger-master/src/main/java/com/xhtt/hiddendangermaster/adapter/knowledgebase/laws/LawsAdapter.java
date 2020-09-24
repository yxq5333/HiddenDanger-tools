package com.xhtt.hiddendangermaster.adapter.knowledgebase.laws;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.laws.Laws;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public class LawsAdapter extends CommonAdapter<Laws> {

    public LawsAdapter(Context context, int layoutId, List<Laws> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Laws item, int position) {
        viewHolder.setText(R.id.tv_title, item.getTitle());
    }

}
