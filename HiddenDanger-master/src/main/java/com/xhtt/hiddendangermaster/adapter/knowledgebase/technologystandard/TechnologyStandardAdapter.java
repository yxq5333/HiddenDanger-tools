package com.xhtt.hiddendangermaster.adapter.knowledgebase.technologystandard;

import android.content.Context;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.technologystandard.TechnologyStandard;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public class TechnologyStandardAdapter extends ZCommonAdapter<TechnologyStandard> {

    public TechnologyStandardAdapter(Context context, int layoutId, List<TechnologyStandard> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, TechnologyStandard item, int position) {
        viewHolder.setText(R.id.tv_title, item.getTitle());
    }

}
