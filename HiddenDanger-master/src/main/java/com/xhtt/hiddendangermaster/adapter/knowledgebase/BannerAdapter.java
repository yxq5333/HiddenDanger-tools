package com.xhtt.hiddendangermaster.adapter.knowledgebase;

import android.content.Context;
import android.view.View;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.listener.ZOnViewClickListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.banner.Banner;
import com.xhtt.hiddendangermaster.util.uploadfile.UploadFileUtils;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;

import java.util.List;

/**
 * @ClassName: Banner适配器
 * @Description:
 * @author: 马禛
 * @date: 2018年09月17日
 */
public class BannerAdapter extends ZCommonAdapter<Banner> {

    private InfiniteScrollAdapter infiniteAdapter;
    private ZOnRecyclerViewItemClickOldListener onBannerClickListener;

    public BannerAdapter(Context context, int layoutId, List<Banner> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, Banner item, int position) {

        Object src;
        if (item.getFileList() != null && item.getFileList().size() > 0) {
            src = UploadFileUtils.webFile2AppFile(item.getFileList().get(0)).getUrl();
        } else {
            if (item.getRes() != null) {
                src = item.getRes();
            } else {
                src = ZCommonResource.getImageError();
            }
        }

        viewHolder.setImageByUrl(R.id.iv_img, src);

        viewHolder.setOnClickListener(R.id.cv_all, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onBannerClickListener != null) {
                    onBannerClickListener.onRecyclerViewItemClick(view, viewHolder, infiniteAdapter.getRealPosition(position));
                }
            }
        });
    }

    public void setInfiniteAdapter(InfiniteScrollAdapter infiniteAdapter) {
        this.infiniteAdapter = infiniteAdapter;
    }

    public void setOnBannerClickListener(ZOnRecyclerViewItemClickOldListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }
}
