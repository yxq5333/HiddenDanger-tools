package com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger;

import android.content.Context;
import android.view.View;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.listener.ZOnViewClickListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class CompanyListAdapter extends ZCommonAdapter<Company> {

    private ZOnRecyclerViewItemClickOldListener onButtonClickListener;

    public CompanyListAdapter(Context context, int layoutId, List<Company> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, Company item, int position) {

        viewHolder.setText(R.id.tv_companyName, item.getCompanyName());
        viewHolder.setText(R.id.tv_mainPeople, item.getMainPeople());
        viewHolder.setText(R.id.tv_mainPeoplePhone, item.getMainPeoplePhone());
        viewHolder.setText(R.id.tv_total, "隐患总数 " + item.getHiddenDangerTotal());
        viewHolder.setText(R.id.tv_changedCount, "" + item.getHiddenDangerChangeCount());
        viewHolder.setText(R.id.tv_unchangedCount, "" + item.getHiddenDangerNoChangeCount());

        viewHolder.setOnClickListener(R.id.cv_all, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_edit, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_hiddenDangerList, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_record, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnLongClickListener(R.id.cv_all, new ZOnViewClickListener(false) {
            @Override
            public void onViewLongClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemLongClick(view, viewHolder, position);
                }
            }
        });
    }

    public void setOnButtonClickListener(ZOnRecyclerViewItemClickOldListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
