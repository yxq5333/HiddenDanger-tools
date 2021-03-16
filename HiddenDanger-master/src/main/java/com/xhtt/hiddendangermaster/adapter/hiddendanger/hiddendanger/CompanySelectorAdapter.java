package com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger;

import android.content.Context;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class CompanySelectorAdapter extends ZCommonAdapter<Company> {

    public CompanySelectorAdapter(Context context, int layoutId, List<Company> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, Company item, int position) {
        viewHolder.setText(R.id.tv_companyName, item.getCompanyName());
    }

}
