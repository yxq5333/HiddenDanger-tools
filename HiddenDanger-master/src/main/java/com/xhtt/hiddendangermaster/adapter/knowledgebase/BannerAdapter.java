package com.xhtt.hiddendangermaster.adapter.knowledgebase;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.request.RequestOptions;
import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.click.OnRecyclerViewItemClickOldListener;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.util.glide.GlideOptions;
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
public class BannerAdapter extends CommonAdapter<Banner> {

    private InfiniteScrollAdapter infiniteAdapter;
    private OnRecyclerViewItemClickOldListener onBannerClickListener;

    public BannerAdapter(Context context, int layoutId, List<Banner> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Banner item, int position) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(HGCommonResource.IMAGE_LOADING)
                .error(HGCommonResource.IMAGE_LOAD_ERROR)
                .centerCrop();

        GlideOptions glideOptions;

        if (item.getFileList() != null && item.getFileList().size() > 0) {
            glideOptions = new GlideOptions(UploadFileUtils.webFile2AppFile(item.getFileList().get(0)).getUrl(), null, GlideOptions.NO_FADE_IN, requestOptions);
        } else {
            if (item.getRes() != null) {
                glideOptions = new GlideOptions(item.getRes(), null, GlideOptions.NO_FADE_IN, requestOptions);
            } else {
                glideOptions = new GlideOptions(HGCommonResource.IMAGE_LOAD_ERROR, null, GlideOptions.NO_FADE_IN, requestOptions);
            }
        }

        viewHolder.setImageByUrl(R.id.iv_img, glideOptions);

        viewHolder.setOnClickListener(R.id.cv_all, new OnViewClickListener(false) {
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

    public void setOnBannerClickListener(OnRecyclerViewItemClickOldListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }
}
