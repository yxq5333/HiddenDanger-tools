package com.xhtt.hiddendangermaster.adapter.knowledgebase.msds;

import com.hg.zero.adapter.list.common.base.ZItemViewDelegate;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.hg.zero.constant.ZConstants;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDSDetail;

/**
 * Created by xhtt on 2017-01-23.
 */

public class ItemMSDSDetailLabel implements ZItemViewDelegate<MSDSDetail> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msds_detail_label;
    }

    @Override
    public boolean isForViewType(MSDSDetail item, int position) {
        return item.getItemType() == ZConstants.LIST_ITEM_TYPE_STICKY;
    }

    @Override
    public void convert(ZViewHolder holder, MSDSDetail item, int position) {
        holder.setText(R.id.tv_title, item.getLabel());
    }

}
