package com.xhtt.hiddendangermaster.adapter.knowledgebase.accidentcase;

import android.content.Context;

import com.bumptech.glide.request.RequestOptions;
import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.hg.hollowgoods.bean.file.AppFile;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.util.glide.GlideOptions;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.knowledgebase.accidentcase.AccidentCase;
import com.xhtt.hiddendangermaster.util.DateUtils;
import com.xhtt.hiddendangermaster.util.uploadfile.UploadFileUtils;
import com.xhtt.hiddendangermaster.util.uploadfile.WebFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 事故案例列表适配器
 * Created by Hollow Goods on 2019-03-29.
 */
public class AccidentCaseAdapter extends CommonAdapter<AccidentCase> {

    public AccidentCaseAdapter(Context context, int layoutId, List<AccidentCase> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, AccidentCase item, int position) {

        viewHolder.setText(R.id.tv_content, item.getTitle());

        ArrayList<WebFile> img = item.getFileList();

        if (img == null || img.size() == 0) {
            viewHolder.setImageResource(R.id.iv_img, HGCommonResource.IMAGE_LOAD_ERROR);
        } else {
            AppFile media = UploadFileUtils.webFile2AppFile(img.get(0));
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(HGCommonResource.IMAGE_LOADING)
                    .error(HGCommonResource.IMAGE_LOAD_ERROR)
                    .centerCrop();
            GlideOptions glideOptions = new GlideOptions(media.getUrl(), null, GlideOptions.NORMAL_FADE_IN, requestOptions);

            viewHolder.setImageByUrl(R.id.iv_img, glideOptions);
        }

        StringBuilder tips = new StringBuilder();
        tips.append(item.getReadCount());
        tips.append("浏览");
        tips.append(" ");
        tips.append(DateUtils.getSmartDate(item.getDate()));
        viewHolder.setText(R.id.tv_tips, tips.toString());
    }

}
