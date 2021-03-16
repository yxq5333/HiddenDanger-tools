package com.xhtt.hiddendangermaster.adapter.knowledgebase.msds;

import android.content.Context;

import com.hg.zero.adapter.list.common.ZMultiItemTypeAdapter;
import com.hg.zero.constant.ZConstants;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDSDetail;
import com.xhtt.hiddendangermaster.constant.Constants;

import java.util.List;

/**
 * Created by xhtt on 2017-01-23.
 */

public class MSDSDetailAdapter extends ZMultiItemTypeAdapter<MSDSDetail> {

    public MSDSDetailAdapter(Context context, List<MSDSDetail> datas) {
        super(context, datas);
        addItemViewDelegate(ZConstants.LIST_ITEM_TYPE_STICKY, new ItemMSDSDetailLabel());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_1, new ItemMSDSDetail01());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_2, new ItemMSDSDetail02());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_3, new ItemMSDSDetail03());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_4, new ItemMSDSDetail04());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_5, new ItemMSDSDetail05());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_6, new ItemMSDSDetail06());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_7, new ItemMSDSDetail07());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_8, new ItemMSDSDetail08());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_9, new ItemMSDSDetail09());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_10, new ItemMSDSDetail10());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_11, new ItemMSDSDetail11());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_12, new ItemMSDSDetail12());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_13, new ItemMSDSDetail13());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_14, new ItemMSDSDetail14());
        addItemViewDelegate(Constants.LIST_ITEM_TYPE_15, new ItemMSDSDetail15());
    }

}
