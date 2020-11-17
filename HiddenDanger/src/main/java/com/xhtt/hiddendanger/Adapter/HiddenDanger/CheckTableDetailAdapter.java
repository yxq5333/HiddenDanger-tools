package com.xhtt.hiddendanger.Adapter.HiddenDanger;

import android.content.Context;
import android.view.View;

import com.hg.hollowgoods.Adapter.BaseRecyclerView.CommonAdapter;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CheckTableContent;
import com.xhtt.hiddendanger.R;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class CheckTableDetailAdapter extends CommonAdapter<CheckTableContent> {

    private boolean isOnlyRead;
    private OnRecyclerViewItemClickListener onStatusChangedListener;

    public CheckTableDetailAdapter(Context context, int layoutId, List<CheckTableContent> data, boolean isOnlyRead) {
        super(context, layoutId, data);
        this.isOnlyRead = isOnlyRead;
    }

    @Override
    protected void convert(ViewHolder viewHolder, CheckTableContent item, int position) {

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
            viewHolder.setOnClickListener(R.id.rb_yes, new OnViewClickListener(false) {
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

            viewHolder.setOnClickListener(R.id.rb_no, new OnViewClickListener(false) {
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

    public void setOnStatusChangedListener(OnRecyclerViewItemClickListener onStatusChangedListener) {
        this.onStatusChangedListener = onStatusChangedListener;
    }
}
