package com.xhtt.hiddendangermaster.adapter.knowledgebase.dangerproduct;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.dangerproduct.DangerProduct;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public class DangerProductAdapter extends CommonAdapter<DangerProduct> {

    public DangerProductAdapter(Context context, int layoutId, List<DangerProduct> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DangerProduct item, int position) {
        viewHolder.setText(R.id.tv_title, item.getNameProd());
    }

}
