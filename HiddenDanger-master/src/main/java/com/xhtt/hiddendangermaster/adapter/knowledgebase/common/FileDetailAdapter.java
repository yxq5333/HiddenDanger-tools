package com.xhtt.hiddendangermaster.adapter.knowledgebase.common;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.hg.hollowgoods.adapter.list.common.CommonAdapter;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.hg.hollowgoods.bean.file.AppFile;
import com.hg.hollowgoods.util.file.FileSelectorUtils;
import com.xhtt.hiddendangermaster.R;

import java.util.List;

/**
 * 文件详情适配器
 * Created by Hollow Goods on 2019-04-03.
 */
public class FileDetailAdapter extends CommonAdapter<AppFile> {

    private FileSelectorUtils fileSelectorUtils;

    public FileDetailAdapter(Context context, int layoutId, List<AppFile> data) {
        super(context, layoutId, data);
        fileSelectorUtils = new FileSelectorUtils();
    }

    @Override
    protected void convert(ViewHolder viewHolder, AppFile item, int position) {

        viewHolder.setImageResource(R.id.iv_icon, fileSelectorUtils.getFileIcon(item.getGenerateName()));

        TextView name = viewHolder.getView(R.id.tv_name);
        StringBuilder sb = new StringBuilder();
        sb.append("<u>");
        sb.append(item.getOriginalName());
        sb.append("</u>");
        name.setText(Html.fromHtml(sb.toString()));

        viewHolder.setVisible(R.id.line, position != mData.size() - 1);
    }
}
