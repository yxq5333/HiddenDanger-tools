package com.xhtt.hiddendangermaster.adapter.knowledgebase.technologystandard;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.technologystandard.TechnologyStandard;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public class TechnologyStandardAdapter extends CommonAdapter<TechnologyStandard> {

    public TechnologyStandardAdapter(Context context, int layoutId, List<TechnologyStandard> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, TechnologyStandard item, int position) {
        viewHolder.setText(R.id.tv_title, item.getTitle());
    }

}
