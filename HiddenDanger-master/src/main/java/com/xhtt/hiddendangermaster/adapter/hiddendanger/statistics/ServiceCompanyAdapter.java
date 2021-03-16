package com.xhtt.hiddendangermaster.adapter.hiddendanger.statistics;

import android.content.Context;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.statistics.ServiceCompany;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-10.
 */
public class ServiceCompanyAdapter extends ZCommonAdapter<ServiceCompany> {

    public ServiceCompanyAdapter(Context context, int layoutId, List<ServiceCompany> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, ServiceCompany item, int position) {

        if (position == 0) {
            viewHolder.setText(R.id.tv_number, "序号");
            viewHolder.setText(R.id.tv_companyName, "企业名称");
            viewHolder.setText(R.id.tv_totalAll, "累计检查");
            viewHolder.setText(R.id.tv_totalNowYear, "本年检查");
        } else {
            viewHolder.setText(R.id.tv_number, position + "");
            viewHolder.setText(R.id.tv_companyName, item.getCompanyName());
            viewHolder.setText(R.id.tv_totalAll, item.getTotalTimes() + "次");
            viewHolder.setText(R.id.tv_totalNowYear, item.getNowYearTimes() + "次");
        }

        viewHolder.setBackgroundRes(R.id.ll_bg, position % 2 == 0 ? R.color.statistics_service_company_item_bg : R.color.white);
    }

}
