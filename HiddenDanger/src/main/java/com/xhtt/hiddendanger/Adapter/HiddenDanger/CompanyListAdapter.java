package com.xhtt.hiddendanger.Adapter.HiddenDanger;

import android.content.Context;
import android.view.View;

import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.hg.hollowgoods.Adapter.BaseRecyclerView.CommonAdapter;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.R;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class CompanyListAdapter extends CommonAdapter<Company> {

    private OnRecyclerViewItemClickListener onButtonClickListener;

    public CompanyListAdapter(Context context, int layoutId, List<Company> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Company item, int position) {

        viewHolder.setText(R.id.tv_companyName, item.getCompanyName());
        viewHolder.setText(R.id.tv_mainPeople, item.getMainPeople());
        viewHolder.setText(R.id.tv_mainPeoplePhone, item.getMainPeoplePhone());
        viewHolder.setText(R.id.tv_total, "隐患总数 " + item.getHiddenDangerTotal());
        viewHolder.setText(R.id.tv_changedCount, "" + item.getHiddenDangerChangeCount());
        viewHolder.setText(R.id.tv_unchangedCount, "" + item.getHiddenDangerNoChangeCount());

        viewHolder.setOnClickListener(R.id.cv_all, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_edit, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_hiddenDangerList, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_record, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnLongClickListener(R.id.cv_all, new OnViewClickListener(false) {
            @Override
            public void onViewLongClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemLongClick(view, viewHolder, position);
                }
            }
        });
    }

    public void setOnButtonClickListener(OnRecyclerViewItemClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
