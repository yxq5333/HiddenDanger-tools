package com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger;

import android.content.Context;
import android.view.View;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.listener.ZOnViewClickListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CheckTableContent;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class CheckTableDetailAdapter extends ZCommonAdapter<CheckTableContent> {

    private boolean isOnlyRead;
    private ZOnRecyclerViewItemClickOldListener onStatusChangedListener;

    public CheckTableDetailAdapter(Context context, int layoutId, List<CheckTableContent> data, boolean isOnlyRead) {
        super(context, layoutId, data);
        this.isOnlyRead = isOnlyRead;
    }

    @Override
    protected void convert(ZViewHolder viewHolder, CheckTableContent item, int position) {

        viewHolder.setText(R.id.tv_checkContent, item.getCheckContent());

        if (item.getStatus() == null) {
            viewHolder.setChecked(R.id.rb_null, true);
        } else {
            if (item.getStatus() == CheckTableContent.STATUS_YES) {
                viewHolder.setChecked(R.id.rb_yes, true);
            } else {
                viewHolder.setChecked(R.id.rb_no, true);
            }
        }

        if (!isOnlyRead) {
            viewHolder.setOnClickListener(R.id.rb_yes, new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    if (item.getStatus() == null || item.getStatus() != CheckTableContent.STATUS_YES) {
                        item.setStatus(CheckTableContent.STATUS_YES);

                        refreshData(mData, position);

                        if (onStatusChangedListener != null) {
                            onStatusChangedListener.onRecyclerViewItemClick(view, viewHolder, position);
                        }
                    }
                }
            });

            viewHolder.setOnClickListener(R.id.rb_no, new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    if (item.getStatus() == null || item.getStatus() != CheckTableContent.STATUS_NO) {
                        item.setStatus(CheckTableContent.STATUS_NO);

                        refreshData(mData, position);

                        if (onStatusChangedListener != null) {
                            onStatusChangedListener.onRecyclerViewItemClick(view, viewHolder, position);
                        }
                    }
                }
            });
        }
    }

    public void setOnStatusChangedListener(ZOnRecyclerViewItemClickOldListener onStatusChangedListener) {
        this.onStatusChangedListener = onStatusChangedListener;
    }
}
