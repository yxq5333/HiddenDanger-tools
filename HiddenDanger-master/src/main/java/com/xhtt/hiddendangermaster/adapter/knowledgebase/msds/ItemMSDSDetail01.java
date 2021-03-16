package com.xhtt.hiddendangermaster.adapter.knowledgebase.msds;

import android.text.TextUtils;

import com.hg.zero.adapter.list.common.base.ZItemViewDelegate;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDSDetail;
import com.xhtt.hiddendangermaster.constant.Constants;

/**
 * Created by xhtt on 2017-01-23.
 */

public class ItemMSDSDetail01 implements ZItemViewDelegate<MSDSDetail> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msds_detail_01;
    }

    @Override
    public boolean isForViewType(MSDSDetail item, int position) {
        return item.getItemType() == Constants.LIST_ITEM_TYPE_1;
    }

    @Override
    public void convert(ZViewHolder holder, MSDSDetail item, int position) {
        holder.setText(R.id.tv_m1, clearFormat(item.getTexts()[0]));
        holder.setText(R.id.tv_m2, clearFormat(item.getTexts()[1]));
        holder.setText(R.id.tv_m3, clearFormat(item.getTexts()[2]));
        holder.setText(R.id.tv_m4, clearFormat(item.getTexts()[3]));
        holder.setText(R.id.tv_m5, clearFormat(item.getTexts()[4]));
        holder.setText(R.id.tv_m6, clearFormat(item.getTexts()[5]));
        holder.setText(R.id.tv_m7, clearFormat(item.getTexts()[6]));
        holder.setText(R.id.tv_m8, clearFormat(item.getTexts()[7]));
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
