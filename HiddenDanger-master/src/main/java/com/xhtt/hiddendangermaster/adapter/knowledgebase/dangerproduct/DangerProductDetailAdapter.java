package com.xhtt.hiddendangermaster.adapter.knowledgebase.dangerproduct;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.dangerproduct.DangerProductDetail;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-03.
 */
public class DangerProductDetailAdapter extends CommonAdapter<DangerProductDetail> {

    public DangerProductDetailAdapter(Context context, int layoutId, List<DangerProductDetail> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, DangerProductDetail item, int position) {
        holder.setText(R.id.tv_label, item.getLabel());
        holder.setText(R.id.tv_content, item.getText());
    }

}
