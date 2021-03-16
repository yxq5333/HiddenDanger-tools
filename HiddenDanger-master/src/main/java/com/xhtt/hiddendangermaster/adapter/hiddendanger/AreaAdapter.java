package com.xhtt.hiddendangermaster.adapter.hiddendanger;

import android.content.Context;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.CommonChooseItem;

import java.util.List;

/**
 * 省市区镇适配器
 * <p>
 * Created by Hollow Goods on 2020-04-08.
 */
public class AreaAdapter extends ZCommonAdapter<CommonChooseItem> {

    private int checkedPosition = -1;

    public AreaAdapter(Context context, int layoutId, List<CommonChooseItem> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, CommonChooseItem item, int position) {
        viewHolder.setText(R.id.tv_area, item.getItem());
        viewHolder.setTextColorRes(R.id.tv_area, position == checkedPosition
                ? R.color.colorAccent
                : R.color.txt_color_dark
        );
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }
}
