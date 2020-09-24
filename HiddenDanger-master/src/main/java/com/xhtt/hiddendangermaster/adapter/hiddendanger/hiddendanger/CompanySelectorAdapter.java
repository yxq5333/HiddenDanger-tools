package com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class CompanySelectorAdapter extends CommonAdapter<Company> {

    public CompanySelectorAdapter(Context context, int layoutId, List<Company> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Company item, int position) {
        viewHolder.setText(R.id.tv_companyName, item.getCompanyName());
    }

}
