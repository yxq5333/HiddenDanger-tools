package com.xhtt.hiddendangermaster.adapter.companymap;

import android.content.Context;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.CompanyMap;

import java.util.List;

/**
 * <p>
 * Created by Hollow Goods on 2020-04-24.
 */
public class MapCompanyListAdapter extends CommonAdapter<CompanyMap> {

    public MapCompanyListAdapter(Context context, int layoutId, List<CompanyMap> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, CompanyMap item, int position) {
        viewHolder.setText(R.id.tv_companyName, item.getCompanyName());
    }
}
