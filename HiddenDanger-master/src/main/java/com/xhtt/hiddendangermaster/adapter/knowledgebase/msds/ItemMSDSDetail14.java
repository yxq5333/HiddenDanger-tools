package com.xhtt.hiddendangermaster.adapter.knowledgebase.msds;

import android.text.TextUtils;

import com.hg.hollowgoods.adapter.list.common.base.ItemViewDelegate;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDSDetail;
import com.xhtt.hiddendangermaster.constant.Constants;


/**
 * Created by xhtt on 2017-01-23.
 */

public class ItemMSDSDetail14 implements ItemViewDelegate<MSDSDetail> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msds_detail_14;
    }

    @Override
    public boolean isForViewType(MSDSDetail item, int position) {
        return item.getItemType() == Constants.LIST_ITEM_TYPE_14;
    }

    @Override
    public void convert(ViewHolder holder, MSDSDetail item, int position) {
        holder.setText(R.id.tv_m1, clearFormat(item.getTexts()[0]));
        holder.setText(R.id.tv_m2, clearFormat(item.getTexts()[1]));
        holder.setText(R.id.tv_m3, clearFormat(item.getTexts()[2]));
        holder.setText(R.id.tv_m4, clearFormat(item.getTexts()[3]));
        holder.setText(R.id.tv_m5, clearFormat(item.getTexts()[4]));
    }

    private String clearFormat(String str) {

        if (TextUtils.isEmpty(str)) {
            return "";
        } else {
            str = str.replaceAll("\n", "");
            str = str.replaceAll("\r", "");
            str = str.replaceAll("\t", "");
            return str;
        }
    }

}
