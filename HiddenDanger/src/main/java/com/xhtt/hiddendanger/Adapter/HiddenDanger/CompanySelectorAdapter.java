package com.xhtt.hiddendanger.Adapter.HiddenDanger;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.hg.hollowgoods.Adapter.BaseRecyclerView.CommonAdapter;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.R;

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
