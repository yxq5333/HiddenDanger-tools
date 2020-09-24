package com.xhtt.hiddendangermaster.adapter.knowledgebase.msds;

import com.hg.hollowgoods.adapter.list.common.base.ItemViewDelegate;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.hg.hollowgoods.constant.HGConstants;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDSDetail;

/**
 * Created by xhtt on 2017-01-23.
 */

public class ItemMSDSDetailLabel implements ItemViewDelegate<MSDSDetail> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msds_detail_label;
    }

    @Override
    public boolean isForViewType(MSDSDetail item, int position) {
        return item.getItemType() == HGConstants.LIST_ITEM_TYPE_HEADER;
    }

    @Override
    public void convert(ViewHolder holder, MSDSDetail item, int position) {
        holder.setText(R.id.tv_title, item.getLabel());
    }

}
