package com.xhtt.hiddendangermaster.adapter.knowledgebase.accidentcase;

import android.content.Context;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.file.ZAppFile;
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
public class AccidentCaseAdapter extends ZCommonAdapter<AccidentCase> {

    public AccidentCaseAdapter(Context context, int layoutId, List<AccidentCase> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, AccidentCase item, int position) {

        viewHolder.setText(R.id.tv_content, item.getTitle());

        ArrayList<WebFile> img = item.getFileList();

        if (img == null || img.size() == 0) {
            viewHolder.setImageResource(R.id.iv_img, ZCommonResource.getImageError());
        } else {
            ZAppFile media = UploadFileUtils.webFile2AppFile(img.get(0));
            viewHolder.setImageByUrl(R.id.iv_img, media.getUrl());
        }

        StringBuilder tips = new StringBuilder();
        tips.append(item.getReadCount());
        tips.append("浏览");
        tips.append(" ");
        tips.append(DateUtils.getSmartDate(item.getDate()));
        viewHolder.setText(R.id.tv_tips, tips.toString());
    }

}
